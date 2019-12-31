package com.example.ml.common;

import java.util.LinkedList;
import java.util.Queue;

public class DataUtils {

    public static Queue<Double> distinct(Queue<Double> queue) {
        Queue<Double> output = new LinkedList<Double>();
        Double last = null;
        for (Double d : queue) {
            if (last == null) {
                last = d;
                output.add(d);
            } else if (last.equals(d)) {
                continue;
            } else {
                last = d;
                output.add(d);
            }
        }
        return output;
    }

    public static Queue<Double> diff(Queue<Double> queue) {
        Queue<Double> output = new LinkedList<Double>();
        Double last = null;
        for (Double d : queue) {
            if (last == null) {
                last = d;
            } else {
                output.add(d - last);
                last = d;
            }
        }
        return output;
    }

    public static double avg(Queue<Double> queue) {
        return sum(queue) / queue.size();
    }

    public static double sum(Queue<Double> queue) {
        Double sum = 0d;
        for (Double d : queue) {
            sum += d;
        }
        return sum ;
    }
}
