package sample.breaker;

import java.util.concurrent.TimeUnit;

/**
 * org.apache.commons.lang3.concurrent.EventCountCircuitBreaker
 * config parameters.
 */
public class BreakerConfig {

    private int openingThreashold;
    private long checkInterval;
    private TimeUnit checkUnit;
    private int closingThreshold;
    private String fallbackMethod;

    public int getOpeningThreashold() {
        return openingThreashold;
    }

    public void setOpeningThreashold(int openingThreashold) {
        this.openingThreashold = openingThreashold;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public TimeUnit getCheckUnit() {
        return checkUnit;
    }

    public void setCheckUnit(TimeUnit checkUnit) {
        this.checkUnit = checkUnit;
    }

    public int getClosingThreshold() {
        return closingThreshold;
    }

    public void setClosingThreshold(int closingThreshold) {
        this.closingThreshold = closingThreshold;
    }

    public String getFallbackMethod() {
        return fallbackMethod;
    }

    public void setFallbackMethod(String fallbackMethod) {
        this.fallbackMethod = fallbackMethod;
    }

    @Override
    public String toString() {
        return "BreakerConfig{" +
                "openingThreashold=" + openingThreashold +
                ", checkInterval=" + checkInterval +
                ", checkUnit=" + checkUnit +
                ", closingThreshold=" + closingThreshold +
                ", fallbackMethod='" + fallbackMethod + '\'' +
                '}';
    }
}
