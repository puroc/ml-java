package com.example.ml.avtest.ar;

import com.example.ml.common.DataUtils;
import com.example.ml.common.FileUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public class ArTest {

    public static final int RIGHT_NUM = 933;
    public static final String FILE_PRIMITIVE_DATA = "/Users/puroc/git/ml-java/src/main/resources/ar/data.csv";
//    public static final String FILE_PRIMITIVE_DATA = "/Users/puroc/git/ml-java/src/main/resources/ar/20191111.csv";
    public static final String FILE_DISTINCT_DATA = "/Users/puroc/git/ml-java/src/main/resources/ar/distinct.csv";
    public static final String FILE_OUT_DATA = "/Users/puroc/git/ml-java/src/main/resources/ar/out.csv";

    public static void main(String[] args) {
        try {

            //读取原始数据
            Queue<Double> data = FileUtils.readFile(FILE_PRIMITIVE_DATA);

            System.out.println("primitive data len:" + data.size());

            //确定移动平均的窗口大小
            int windowSize = RIGHT_NUM;

            //窗口队列
            WindowQueue windowQueue = new WindowQueue(windowSize);

            //输出队列
            Queue<Double> outDataQueue = new LinkedList<Double>();

            //数据去重
            data = DataUtils.distinct(data);

            //去重后的数据写入文件
            FileUtils.writeFile(data, new File(FILE_DISTINCT_DATA));

            AtomicLong num = new AtomicLong();

            double ar = 0d;
            double sde = 0d;
            double max = 0d;
            double last = 0d;

            for (double x : data) {

                //当前值的序号
                long currentNum = num.incrementAndGet();

                //当前值的序号大于窗口长度时才开始进行异常值检测
                if (currentNum > windowSize) {

                    //计算出当前数据与上一条数据的差
                    double diff = x - last;

                    //x是当前值，max是当前值的预测最大值，若x大于max则认为是异常值
                    if (diff > max) {
                        System.out.println("wrong number = " + x + ", ar = " + ar + ",sde = " + sde + " ,max = " + max);
                        continue;
                    }
                }
                //将正确的数据放入输出队列
                outDataQueue.add(x);

                //将数据放入窗口队列中
                windowQueue.addData(x);

                //当达到窗口长度时，开始对下一个差分值进行预测
                if (currentNum >= windowSize) {
                    //将窗口中的数据进行差分，得到一个差分后的队列
                    Queue<Double> diffQueue = DataUtils.diff(windowQueue.getQueue());

                    //将窗口差分队列中的数据拷贝到移动平均算法中
                    SimpleMovingAverage sma = new SimpleMovingAverage(windowSize);
                    sma.setQueue(diffQueue);

                    //计算窗口差分队列中数据的平均值
                    ar = sma.getMovingAverage();

                    //计算窗口差分队列中数据的标准误差
                    sde = sma.getSde();

                    //预测下一个差分值的数值上限
                    max = ar + sde;
                }

                //将当前值赋值给last，用于跟下一个数据做差
                last = x;
            }

            FileUtils.writeFile(outDataQueue, new File(FILE_OUT_DATA));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
