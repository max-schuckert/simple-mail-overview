package com.max.mailoverview;

public class Counter {
    private int current = 0;

    public synchronized void add(){
        current++;
    }

    public int getCurrentValue(){
        return current;
    }
}
