package sample.breaker;

public class BreakerException extends RuntimeException {
    public BreakerException(String message) {
        super(message);
    }

    public BreakerException(String message, Throwable cause) {
        super(message, cause);
    }
}
