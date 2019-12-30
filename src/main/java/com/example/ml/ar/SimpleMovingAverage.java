package com.example.ml.ar;
/**
 * SimpleMovingAverage
 * 使用队列实现POJO移动平均
 */

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.Queue;

public class SimpleMovingAverage {

    private double sum = 0.0;
    private int period;
    private final Queue<Double> window = new LinkedList<Double>();

    public SimpleMovingAverage(int period) {
        if (period < 1) {
            throw new IllegalArgumentException("period must be > 0");
        }
        this.period = period;
    }

    public static double avg(Queue<Double> diffQueue100) {
        Double sum = 0d;
        for (Double d : diffQueue100) {
            sum += d;
        }
        return sum / diffQueue100.size();
    }

    public Queue<Double> getQueue() {
        return window;
    }

    public void addNewNumber(double number) {
        sum += number;
        window.add(number);
        if (window.size() > period) {
            sum -= window.remove();
        }
    }

    public double getMovingAverage() {
        if (window.isEmpty()) {
            throw new IllegalArgumentException("average is undefined");
        }
        return sum / window.size();
    }

    public double getSde() {
        //标准误差
        //        return 1.96 * sd(window) / sqrt(period);
        //三倍标准差
        return 3 * sd(window);
    }

    //方差s^2=[(x1-x)^2 +...(xn-x)^2]/n 或者s^2=[(x1-x)^2 +...(xn-x)^2]/(n-1)
    public static double var(double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return dVar / m;
    }

    //标准差σ=sqrt(s^2)
    public static double sd(Queue<Double> queue) {
        Double[] x = new Double[queue.size()];
        queue.toArray(x);
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        double dAve = sum / m;//求平均值
        double dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        //reture Math.sqrt(dVar/(m-1));
        return Math.sqrt(dVar / m);
    }

    public static double sqrt(double b) {
        double result = 0;
        if (b < 1 && b > 0) {
            double lower = b;
            double upper = 1;
            int time = 0;
            while (true) {

                if ((lower + upper) * (lower + upper) / 4 == b) {
                    return (lower + upper) / 2;
                } else if ((lower + upper) * (lower + upper) / 4 > b) {
                    upper = (lower + upper) / 2;
                } else {
                    lower = (lower + upper) / 2;
                }
                time++;
                if (time > 1000) {
                    result = (lower + upper) / 2;
                    break;
                }
            }
        } else if (b >= 1) {
            double lower = 1;
            double upper = b;
            int time = 0;
            while (true) {

                if ((lower + upper) * (lower + upper) / 4 == b) {
                    return (lower + upper) / 2;
                } else if ((lower + upper) * (lower + upper) / 4 > b) {
                    upper = (lower + upper) / 2;
                } else {
                    lower = (lower + upper) / 2;
                }
                time++;
                if (time > 1000) {
                    result = (lower + upper) / 2;
                    break;
                }
            }
        } else if (b == 0) {
            result = 0;
        } else {
            System.out.println("NaN");
            System.exit(0);
        }
        return result;
    }

    private static BigDecimal sqrtIteration(BigDecimal x, BigDecimal n) {
        return x.add(n.divide(x, MathContext.DECIMAL128)).divide(new BigDecimal("2"), MathContext.DECIMAL128);
    }


}

