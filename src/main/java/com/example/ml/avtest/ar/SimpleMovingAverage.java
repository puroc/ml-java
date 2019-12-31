package com.example.ml.avtest.ar;
/**
 * SimpleMovingAverage
 * 使用队列实现POJO移动平均
 */

import com.example.ml.common.DataUtils;
import com.example.ml.common.MathUtils;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private Queue<Double> queue = new LinkedList<Double>();
    private int windowSize;

    public SimpleMovingAverage(int windowSize){
        this.windowSize = windowSize;
    }

    public void setQueue(Queue<Double> queue) {
        this.queue = queue;
    }

    public Queue<Double> getQueue() {
        return queue;
    }

    public double getMovingAverage() {
        if (queue.isEmpty()) {
            throw new IllegalArgumentException("average is undefined");
        }
        return DataUtils.sum(queue) / queue.size();
    }

    public double getSde() {
        //标准误差
//        return 1.96 * MathUtils.sd(queue) / MathUtils.sqrt(windowSize);
        //三倍标准差
        return 3 * MathUtils.sd(queue);
    }

}

