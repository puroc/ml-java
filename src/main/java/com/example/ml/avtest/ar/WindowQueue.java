package com.example.ml.avtest.ar;

import java.util.LinkedList;
import java.util.Queue;

public class WindowQueue {

    private int windowSize;

    private Queue<Double> queue = new LinkedList<Double>();

    public WindowQueue(int windowSize) {
        if (windowSize < 1) {
            throw new IllegalArgumentException("windowSize must be > 0");
        }
        this.windowSize = windowSize;
    }

    public void addData(double number) {
        queue.add(number);
        if (queue.size() > windowSize) {
            queue.remove();
        }
    }

    public Queue<Double> getQueue(){
        return queue;
    }


}
