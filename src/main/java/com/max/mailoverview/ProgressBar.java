package com.max.mailoverview;

public class ProgressBar implements Runnable {
    private final int total;
    private final Counter counter;

    public ProgressBar(int total, Counter counter){
        this.total = total;
        this.counter = counter;
    }

    @Override
    public void run() {
        while (counter.getCurrentValue() < total){
            try {
                System.out.printf("\r Progress: %s %s/%s (%.0f%%)",getProgressBar(counter.getCurrentValue(), total, 25), counter.getCurrentValue(), total, (float) counter.getCurrentValue() * 100 / total);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("\r Progress: %s %s/%s (%.0f%%)%n",getProgressBar(counter.getCurrentValue(), total, 25), counter.getCurrentValue(), total, (float) counter.getCurrentValue() * 100 / total);
    }

    private String getProgressBar(int currentValue, int total, int size){
        StringBuilder bar = new StringBuilder();
        bar.append("[");

        double percentage = (double) currentValue * 100 / total;
        int barsDone = (int) (Math.floor(percentage / (100 / (double) size)));

        bar.append("#".repeat(barsDone));
        bar.append(" ".repeat(size - barsDone));

        bar.append("]");

        return bar.toString();
    }
}
