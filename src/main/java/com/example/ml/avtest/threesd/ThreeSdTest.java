package com.example.ml.avtest.threesd;

import com.example.ml.common.DataUtils;
import com.example.ml.common.FileUtils;
import com.example.ml.common.MathUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class ThreeSdTest {

    //基于去重之后的数据的正确样本数量
    public static final int RIGHT_NUM = 933;
    public static final String FILE_PRIMITIVE_DATA = "/Users/puroc/git/ml-java/src/main/resources/threesd/data.csv";
//    public static final String FILE_PRIMITIVE_DATA = "/Users/puroc/git/ml-java/src/main/resources/threesd/20191111.csv";
    public static final String FILE_OUT_DATA = "/Users/puroc/git/ml-java/src/main/resources/threesd/out.csv";
    public static final String FILE_DISDINCT_DATA = "/Users/puroc/git/ml-java/src/main/resources/threesd/distinct.csv";

    public static void main(String[] args) {
        try {
            Queue<Double> data = FileUtils.readFile(FILE_PRIMITIVE_DATA);

            System.out.println("primitive data len:" + data.size());

            Queue<Double> outDataQueue = new LinkedList<Double>();

            //对原数据进行去重，因为原始数据中很多数据可能是相同的，这样取出的样本数据，差分后的均值可能是0（所以3倍标准差也是0），这样就无法进行异常值判断了
            data = DataUtils.distinct(data);

            //去重后的数据写入文件
            FileUtils.writeFile(data, new File(FILE_DISDINCT_DATA));

            //取前若干个样本数据
            Double lastInRightData = null;
            Queue<Double> rightData = new LinkedList<Double>();
            for (int i = 0; i < RIGHT_NUM; i++) {
                Double d = data.poll();
                rightData.add(d);
                if (i == RIGHT_NUM - 1) {
                    lastInRightData = d;
                }
            }

            // 先对样本数据做差分，再计算3倍标准差
            Queue<Double> diffRightData = DataUtils.diff(rightData);
            double avg = DataUtils.avg(diffRightData);
            double max = avg + 3 * MathUtils.sd(diffRightData);

            //将样本数据放入输出的队列中
            for (Double d : rightData) {
                outDataQueue.add(d);
            }

            //进行异常值比对，丢弃异常值，保留正确值
            Double last = lastInRightData;
            for (Double d : data) {
                //计算当前值与上一个值的差
                Double diff = d - last;

                //如果差大于之前差分后的样本数据的3倍标注差，则认为是异常值，异常值丢弃
                if (diff > max) {
                    continue;
                } else {
                    //若值正常，则添加到输出队列
                    outDataQueue.add(d);
                    //更新last，用于跟下个值作比较
                    last = d;
                }
            }
            FileUtils.writeFile(outDataQueue, new File(FILE_OUT_DATA));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
