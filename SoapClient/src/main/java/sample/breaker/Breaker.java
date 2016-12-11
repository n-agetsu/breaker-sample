package sample.breaker;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@InterceptorBinding
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Breaker {
    @Nonbinding int openingThreshold() default 5;
    @Nonbinding long checkInterval() default 1;
    @Nonbinding TimeUnit checkUnit() default TimeUnit.MINUTES;
    @Nonbinding int closingThreshold() default 5;
    @Nonbinding  String fallbackMethod() default "";
}
