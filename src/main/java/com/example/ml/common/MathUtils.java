package com.example.ml.common;

import java.util.Queue;

public class MathUtils {

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

    //开根号
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
}
