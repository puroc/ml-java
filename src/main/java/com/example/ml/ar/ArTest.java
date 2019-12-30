package com.example.ml.ar;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public class ArTest {

    public static final int RIGHT_NUM = 1000;

    public static void main(String[] args) {
        try {
            Queue<Double> testData = readFile();

            //原始数据
            writeFile(testData, new File("/Users/puroc/git/ml-java/src/main/resources/ar/primitive.csv"));

            testData = distinct(testData);

            //去重后的数据
            writeFile(testData, new File("/Users/puroc/git/ml-java/src/main/resources/ar/distinct.csv"));

//            testData = diff(testData);
//
//            //差分后的数据
//            writeFile(testData, new File("/Users/puroc/git/ml-java/src/main/resources/ar/diff.csv"));

            Queue<Double> outDataQueue = new LinkedList<Double>();

            System.out.println("primitive data len:" + testData.size());

            AtomicLong num = new AtomicLong();

            //取前100个数据，先做差分，再计算3倍标准差

            Double lastIn100 = null;
            Queue<Double> right100 = new LinkedList<Double>();
            for (int i = 0; i < RIGHT_NUM; i++) {
                Double d = testData.poll();
                right100.add(d);
                if (i == RIGHT_NUM - 1) {
                    lastIn100 = d;
                }
            }

            for (Double d : right100) {
                outDataQueue.add(d);
            }

            Queue<Double> diffQueue100 = diff(right100);
            double avg100 = SimpleMovingAverage.avg(diffQueue100);
            double max = avg100 + 3 * SimpleMovingAverage.sd(diffQueue100);

            Double last = lastIn100;
            for (Double d : testData) {
                Double diff = d - last;

                //异常值
                if (diff > max) {
                    continue;
                } else {
                    outDataQueue.add(d);
                    last = d;
                }
            }

//            //使用队列
//            for (int windowSize : allWindowSizes) {
//                SimpleMovingAverage sma = new SimpleMovingAverage(windowSize);
//                System.out.println("winSize = " + windowSize);
//
//                double ar = 0d;
//                double sde = 0d;
//                double max = 0d;
//
//                for (double x : testData) {
//                    long currentNum = num.incrementAndGet();
//
//                    //当前值的序号大于窗口长度时才开始进行异常值检测
//                    if (currentNum > windowSize) {
//                        //x是当前值，max是当前值的预测最大值，若x大于max则认为是异常值
//                        if (x > max) {
//                            System.out.println("wrong number = " + x + ", ar = " + ar + ",sde = " + sde + " ,max = " + max);
//                            continue;
//                        }
//                    }
//                    outDataQueue.add(x);
//                    //进行下一个值的预测
//                    sma.addNewNumber(x);
//                    if (currentNum >= windowSize) {
//                        Queue<Double> diffQueue = diff(sma.getQueue());
//                        diffQueue
//                                ar = sma.getMovingAverage();
//                        sde = sma.getSde();
//                        max = ar + sde;
//                    }
//                }
//            }
            writeFile(outDataQueue, new File("/Users/puroc/git/ml-java/src/main/resources/ar/out.csv"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Queue<Double> readFile() {
        BufferedReader bufferedReader = null;
        Queue<Double> queue = new LinkedList<Double>();
        try {
            FileReader fileReader = new FileReader("/Users/puroc/git/ml-java/src/main/resources/ar/a.csv");
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                double ce = Double.parseDouble(line.split(",")[0]);
                queue.add(ce);
                line = bufferedReader.readLine();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return queue;
    }

    private static Queue<Double> distinct(Queue<Double> queue) {
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

    private static Queue<Double> diff(Queue<Double> queue) {
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

    private static void writeFile(Queue<Double> queue, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        try {
            // 创建文件
            file.createNewFile();
            // 向文件写入内容
            for (Double d : queue) {
                writer.write(d + "\n");
            }
            writer.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
