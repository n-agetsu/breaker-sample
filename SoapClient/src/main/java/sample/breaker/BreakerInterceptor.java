package sample.breaker;

import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Breaker
public class BreakerInterceptor {

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        BreakerHolder holder = Breakers.get().breaker(ic.getMethod());
        EventCountCircuitBreaker breaker = holder.breaker();

        if (breaker.checkState()) {
            try {
                return ic.proceed();
            } catch (Exception e) {
                breaker.incrementAndCheckState();
                throw e;
            }
        }

        // exec fallbackMethod
        Method fallback = holder.fallback();
        return fallback.invoke(ic.getTarget(), ic.getParameters());
    }
}
