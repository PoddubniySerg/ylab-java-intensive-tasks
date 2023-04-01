package io.ylab.intensive.lesson02.rate.limited.printer;

public class RateLimitedPrinter {

    private final int interval;
    private long lastPrint;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
        this.lastPrint = 0;
    }

    public void print(String message) {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - lastPrint > interval) {
            System.out.println(message);
            lastPrint = System.currentTimeMillis();
        }
    }
}
