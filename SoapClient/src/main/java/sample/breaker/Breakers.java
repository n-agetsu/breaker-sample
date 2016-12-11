package sample.breaker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Hold breakers.
 */
public class Breakers {

    private static final Breakers self = new Breakers();
    Map<Method, BreakerHolder> breakers = new HashMap<>();

    private Breakers() {
        // singleton
    }

    public static Breakers get() {
        return self;
    }

    public BreakerHolder breaker(Method method) {
        return breakers.get(method);
    }

    public BreakerHolder put(Method method, BreakerHolder breaker) {
        return breakers.put(method, breaker);
    }
}
