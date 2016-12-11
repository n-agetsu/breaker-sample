package sample.breaker;

import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scan @Breaker interceptor binding annotations,
 * and Initialize EventCountCircuitBreaker.
 */
public class BreakerExtention implements Extension {

    private static final Logger LOG = LoggerFactory.getLogger(BreakerExtention.class);
    private static final Logger BREAKER_LOG = LoggerFactory.getLogger(BreakerInterceptor.class);

    <T> void processAnnotatedType(@Observes @WithAnnotations({Breaker.class}) ProcessAnnotatedType<T> pat) {
        Breakers breakers = Breakers.get();
        if (!pat.getAnnotatedType().getJavaClass().equals(BreakerInterceptor.class)) {
            pat.getAnnotatedType().getMethods().stream()
                    .filter(method -> method.isAnnotationPresent(Breaker.class))
                    .map(method -> method.getJavaMember())
                    .forEach(method -> {
                        BreakerConfig config = createConfig(method);
                        Method fallbackMethod = getFallbackMethod(method.getDeclaringClass(), config.getFallbackMethod());
                        String fqcn = fqcn(method);
                        breakers.put(method, createBreakerHolder(config, fallbackMethod, fqcn));
                        LOG.info("breaker created for " +  fqcn + ", now state is close. " + config);
                    });
        }
    }

    private BreakerConfig createConfig(Method method) {
        Breaker breaker = method.getAnnotation(Breaker.class);
        BreakerConfig config = new BreakerConfig();
        config.setOpeningThreashold(breaker.openingThreshold());
        config.setCheckInterval(breaker.checkInterval());
        config.setCheckUnit(breaker.checkUnit());
        config.setClosingThreshold(breaker.closingThreshold());
        config.setFallbackMethod(breaker.fallbackMethod());
        return config;
    }

    private Method getFallbackMethod(Class<?> declaredClass, String fallbackMethodName) {
        List<Method> fallbackMethods = Arrays.stream(declaredClass.getDeclaredMethods())
                .filter(method -> fallbackMethodName.equals(method.getName()))
                .collect(Collectors.toList());

        if (fallbackMethods.isEmpty()) {
            throw new BreakerException(
                    "CircuitBreaker init failed, fallbackMethod " + fallbackMethodName + " not found "
                    + "in " + declaredClass.getName());
        } else if (fallbackMethods.size() > 1) {
            throw new BreakerException(
                    "fallbackMethod support only one method. "
                    + "but in " + declaredClass.getName() + " " + fallbackMethodName + " is " + fallbackMethods.size());
        }

        Method fallbackMethod = fallbackMethods.get(0);
        fallbackMethod.setAccessible(true);
        return fallbackMethod;
    }

    private BreakerHolder createBreakerHolder(BreakerConfig config, Method fallback, String targetFqcn) {
        EventCountCircuitBreaker breaker = new EventCountCircuitBreaker(config.getOpeningThreashold(),
                config.getCheckInterval(), config.getCheckUnit(), config.getClosingThreshold());

        // state change log category sample.breaker.BreakerInterceptor
        breaker.addChangeListener(event -> {
            if (Boolean.valueOf(event.getNewValue().toString())) {
                BREAKER_LOG.warn("breaker state change: open " + targetFqcn);
            } else {
                BREAKER_LOG.info("breaker stage change: close " + targetFqcn);
            }
        });
        return new BreakerHolder(breaker, fallback, targetFqcn);
    }

    private String fqcn(Method target) {
        return target.getDeclaringClass().getCanonicalName() + "." + target.getName();
    }
}