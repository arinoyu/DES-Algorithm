package com.arinoyu.des;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyDES {

    // 初始置换
    private final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    // 逆初始置换
    private final int[] IP_1 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    //E扩展
    private final int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    //P置换
    private final int[] P = {16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25};

    // 将RE与K进行异或运算并将异或结果利用数据表2中的S_BOX进行S盒替换，得到48位的运算结果记为RS
    private static final int[][][] S_BOX = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}
    };

    //PC-1
    private final int[] PC1 = {57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};

    //PC-2
    private final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32};

    /**
     * 1. 28位的C0和28位的D0分别根据leftShiftArray数组相应位置的值进行左移位得到C1和D1
     * 2. C1和D1合并根据PC2进行压缩置换得到48位的子密钥
     * 3. C1和D1作为下轮的输入以用来产生下一个子密钥
     */

    private final int[] leftShiftArray = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    // 16个子密钥
    private final int[][] subKey = new int[16][48];

    public MyDES(String key) {

        // 生成16个子密钥
        generateKeys(key);
    }

    // 加密
    public static byte[] encode(byte[] data, String key) {
        MyDES myDES = new MyDES(key);
        return myDES.separateBlocks(data, true);
    }

    // 解密
    public static byte[] decode(byte[] data, String key) {
        MyDES myDES = new MyDES(key);
        return myDES.separateBlocks(data, false);
    }

    // 拆分分组
    public byte[] separateBlocks(byte[] data, boolean flag) {
        int originLength = data.length;
        int blockNum;
        int restNum;
        blockNum = originLength / 8;

        restNum = 8 - (originLength - blockNum * 8);
        byte[] dataPadding;

        /*
          填充方案-PKCS7Padding：
          假设数据长度需要填充n(n>0)个字节才对齐，
          那么填充n个字节，每个字节都是n;如果数据本身就已经对齐了，
          则填充一块长度为块大小的数据，每个字节都是块大小。
         */
        if (restNum < 8) {
            dataPadding = new byte[originLength + restNum];
            System.arraycopy(data, 0, dataPadding, 0, originLength);

            for (int i = 0; i < restNum; i++) {
                dataPadding[originLength + i] = (byte) restNum;
            }
        } else {
            dataPadding = new byte[originLength + 8];
            System.arraycopy(data, 0, dataPadding, 0, originLength);

            for (int i = 0; i < restNum; i++) {
                dataPadding[originLength + i] = (byte) 8;
            }
        }

        if (!flag) {
            dataPadding = data;
        }

        // 将原字节流分成以8个字节为一个block单位，对每个block进行单独处理
        blockNum = dataPadding.length / 8;
        byte[] dataBlock = new byte[8];
        byte[] resultData = new byte[dataPadding.length];
        for (int i = 0; i < blockNum; i++) {
            System.arraycopy(dataPadding, i * 8, dataBlock, 0, 8);
            System.arraycopy(processBlock(dataBlock, subKey, flag), 0, resultData, i * 8, 8);
        }

        // 解密
        if (!flag) {
            int deleteNum = resultData[resultData.length - 1];
            int lengthAfterDelete = resultData.length - deleteNum;

            byte[] dataDecodeResult;
            try {
                dataDecodeResult = new byte[lengthAfterDelete];
                System.arraycopy(resultData, 0, dataDecodeResult, 0, lengthAfterDelete);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("密钥错误");
                return null;
            }
            return dataDecodeResult;
        }
        return resultData;

    }

    // 处理一个Block，用flag标识是进行加密还是解密操作
    public byte[] processBlock(byte[] data, int[][] subKey, boolean flag) {
        int[] dataBits = new int[64];

        // 将byte数组转换为string再转为64bit整数数组
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            StringBuilder data_block = new StringBuilder(Integer.toBinaryString(data[i] & 0xff));
            while (data_block.length() % 8 != 0) {
                data_block.insert(0, "0");
            }
            stringBuilder.append(data_block);
        }
        String data_string = stringBuilder.toString();
        for (int i = 0; i < 64; i++) {
            int dataChar = data_string.charAt(i);
            if (dataChar == 48) {
                dataChar = 0;
            } else if (dataChar == 49) {
                dataChar = 1;
            } else {
                System.out.println("转换bit错误!");
            }
            dataBits[i] = dataChar;
        }

        // IP置换
        int[] dataIP = new int[64];
        for (int i = 0; i < 64; i++) {
            dataIP[i] = dataBits[IP[i] - 1];
        }

        // 加密
        if (flag) {
            for (int i = 0; i < 16; i++) {
                leftShift(dataIP, i, true, subKey[i]);
            }
            // 解密
        } else {
            for (int i = 15; i > -1; i--) {
                leftShift(dataIP, i, false, subKey[i]);
            }
        }
        int[] temp = new int[64];
        for (int i = 0; i < IP_1.length; i++) {
            temp[i] = dataIP[IP_1[i] - 1];
        }

        // 将64位int数组转换回8byte数组
        byte[] tempByte = new byte[8];
        for (int i = 0; i < 8; i++) {
            tempByte[i] = (byte) ((temp[8 * i] << 7) + (temp[8 * i + 1] << 6) + (temp[8 * i + 2] << 5) + (temp[8 * i + 3] << 4) + (temp[8 * i + 4] << 3) + (temp[8 * i + 5] << 2) + (temp[8 * i + 6] << 1) + (temp[8 * i + 7]));
        }
        return tempByte;

    }

    public void leftShift(int[] M, int times, boolean flag, int[] keyArray) {

        // 将M分割成L0和R0分别进行处理
        int[] L0 = new int[32];
        int[] R0 = new int[32];
        int[] L1;
        int[] R1 = new int[32];
        int[] f;
        System.arraycopy(M, 0, L0, 0, 32);
        System.arraycopy(M, 32, R0, 0, 32);
        L1 = R0;

        // 将R0与子密钥进行function变换得到f
        f = functionF(R0, keyArray);

        for (int j = 0; j < 32; j++) {

            // 在16轮迭代的最后一轮，加密和解密的左右子密钥是相反的
            R1[j] = L0[j] ^ f[j];
            if (((!flag) && (times == 0)) || ((flag) && (times == 15))) {
                M[j] = R1[j];
                M[j + 32] = L1[j];
            } else {
                M[j] = L1[j];
                M[j + 32] = R1[j];
            }
        }

    }

    public int[] functionF(int[] dataRight, int[] key) {
        int[] result = new int[32];
        int[] EK = new int[48];
        for (int i = 0; i < E.length; i++) {
            EK[i] = dataRight[E[i] - 1] ^ key[i];
        }

        // S盒替换: 由48位变32位，先分割EK，然后再进行替换
        int[][] S = new int[8][6];
        int[] sBoxAfter = new int[32];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(EK, i * 6, S[i], 0, 6);

            // 横坐标
            int x = (S[i][0] << 1) + S[i][5];

            // 纵坐标
            int y = (S[i][1] << 3) + (S[i][2] << 2) + (S[i][3] << 1) + S[i][4];

            StringBuilder str = new StringBuilder(Integer.toBinaryString(S_BOX[i][x][y]));
            while (str.length() < 4) {
                str.insert(0, "0");
            }
            for (int j = 0; j < 4; j++) {
                int c = str.charAt(j);
                if (c == 48) {
                    c = 0;
                } else if (c == 49) {
                    c = 1;
                } else {
                    System.out.println("转换bit错误!");
                }
                sBoxAfter[4 * i + j] = c;
            }

        }

        // S盒替换结束，P盒替代
        for (int i = 0; i < P.length; i++) {
            result[i] = sBoxAfter[P[i] - 1];
        }
        return result;

    }

    // 生成子密钥
    public void generateKeys(String key) {

        // 将key转化为8个字节，如果不够就复制原key加到后面，超过就截取前8个字节
        StringBuilder keyBuilder = new StringBuilder(key);
        while (keyBuilder.length() < 8) {
            keyBuilder.append(keyBuilder);
        }
        key = keyBuilder.toString();
        key = key.substring(0, 8);
        byte[] keys = key.getBytes();
        int[] keyBits = new int[64];

        // 将key从string转换到字节数组再转换到int数组
        for (int i = 0; i < 8; i++) {
            StringBuilder keyString = new StringBuilder(Integer.toBinaryString(keys[i] & 0xff));
            if (keyString.length() < 8) {
                int keyStringLength = keyString.length();
                for (int t = 0; t < 8 - keyStringLength; t++) {
                    keyString.insert(0, "0");
                }
            }
            for (int j = 0; j < 8; j++) {
                int keyChar = keyString.charAt(j);
                if (keyChar == 48) {
                    keyChar = 0;
                } else if (keyChar == 49) {
                    keyChar = 1;
                } else {
                    System.out.println("转换bit错误!");
                }
                keyBits[i * 8 + j] = keyChar;
            }
        }

        // keyBits是初始的64位长密钥，下一步开始进行替换
        // PC-1压缩
        int[] keyNewBits = new int[56];
        for (int i = 0; i < PC1.length; i++) {
            keyNewBits[i] = keyBits[PC1[i] - 1];
        }
        int[] C0 = new int[28];
        int[] D0 = new int[28];
        System.arraycopy(keyNewBits, 0, C0, 0, 28);
        System.arraycopy(keyNewBits, 28, D0, 0, 28);

        // 进行16次左位移得到16个子密钥
        for (int i = 0; i < 16; i++) {
            int[] C1 = new int[28];
            int[] D1 = new int[28];
            if (leftShiftArray[i] == 1) {
                System.arraycopy(C0, 1, C1, 0, 27);
                C1[27] = C0[0];
                System.arraycopy(D0, 1, D1, 0, 27);
                D1[27] = D0[0];
            } else if (leftShiftArray[i] == 2) {
                System.arraycopy(C0, 2, C1, 0, 26);
                C1[26] = C0[0];
                C1[27] = C0[1];
                System.arraycopy(D0, 2, D1, 0, 26);
                D1[26] = D0[0];
                D1[27] = D0[1];
            } else {
                System.out.println("左移位错误!");
            }
            int[] tmp = new int[56];
            System.arraycopy(C1, 0, tmp, 0, 28);
            System.arraycopy(D1, 0, tmp, 28, 28);

            // PC2压缩置换
            for (int j = 0; j < PC2.length; j++) {
                subKey[i][j] = tmp[PC2[j] - 1];
            }
            C0 = C1;
            D0 = D1;
        }
    }
}
