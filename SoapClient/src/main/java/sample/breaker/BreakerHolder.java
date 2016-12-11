package sample.breaker;

import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

import java.lang.reflect.Method;

/**
 * Breaker instance and fallbackMethod for reflection object cache.
 */
public class BreakerHolder {
    private EventCountCircuitBreaker breaker;
    private Method fallback;
    private String fqcn;  // for debug

    public BreakerHolder(EventCountCircuitBreaker breaker, Method fallback, String fqcn) {
        this.breaker = breaker;
        this.fallback = fallback;
        this.fqcn = fqcn;
    }

    public EventCountCircuitBreaker breaker() {
        return breaker;
    }

    public Method fallback() {
        return fallback;
    }

    public String fqcn() {
        return fqcn;
    }
}
