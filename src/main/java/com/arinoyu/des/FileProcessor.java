package com.arinoyu.des;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileProcessor {

    // 加密文件并输出
    public static void encodeFile(String inputFilePath, String outputFilePath, String key) {
        processFile(inputFilePath, outputFilePath, key, true);
    }

    // 解密文件并输出
    public static void decodeFile(String inputFilePath, String outputFilePath, String key) {
        processFile(inputFilePath, outputFilePath, key, false);
    }

    private static void processFile(String inputFilePath, String outputFilePath, String key, boolean flag) {

        FileInputStream fis;
        FileOutputStream fos;
        try {
            fis = new FileInputStream(inputFilePath);
            fos = new FileOutputStream(outputFilePath);

            int bufferSize;

            // 这里考虑到了填充方案-PKCS7Padding的问题
            // 加密的时候每次读1024个，都会加上8个变成1032
            // 所以解密的时候每次要读1032个
            if (flag) {
                bufferSize = 1024;
            } else {
                bufferSize = 1032;
            }

            byte[] buffer = new byte[bufferSize];
            int count;
            while ((count = fis.read(buffer)) != -1) {

                // 如果读入字节数小于buffer数组长度，截取字节流
                byte[] bytes = new byte[count];

                System.arraycopy(buffer, 0, bytes, 0, count);

                if (flag) {
                    byte[] encodedBytes = MyDES.encode(bytes, key);
                    fos.write(encodedBytes);
                } else {
                    byte[] decodedBytes = MyDES.decode(bytes, key);
                    fos.write(decodedBytes);
                }

            }
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
