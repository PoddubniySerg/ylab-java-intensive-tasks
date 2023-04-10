package io.ylab.intensive.lesson02.stats.accumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {

    private int count;
    private int min;
    private int max;
    private int sum;

    @Override
    public void add(int value) {
        ++count;
        sum += value;
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return (double) sum / count;
    }
}
