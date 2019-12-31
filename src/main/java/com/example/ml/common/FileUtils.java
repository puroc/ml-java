package com.example.ml.common;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class FileUtils {

    public static void writeFile(Queue<Double> queue, File file) throws IOException {
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

    public static Queue<Double> readFile(String path) {
        BufferedReader bufferedReader = null;
        Queue<Double> queue = new LinkedList<Double>();
        try {
            FileReader fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                double ce = Double.parseDouble(line.split(",")[1]);
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
}
