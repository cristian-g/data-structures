package controller;

public class Timer {
    private long startTime;
    private long endTime;

    public Timer() {
        this.triggerStart();
    }

    public void triggerStart() {
        this.startTime = System.nanoTime();
    }

    public void triggerEnd() {
        this.endTime = System.nanoTime();
    }

    public double computeDuration() {
        return (this.endTime - this.startTime) / 1000000.0;
    }

    public String computeFormattedDuration() {
        return "TIME: " + this.computeDuration() + " milliseconds";
    }
}
