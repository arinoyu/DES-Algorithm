package com.arinoyu.des;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 网络传输字符串时，必须使用Base64编码，
 * 因为如果使用UTF-8或其他编码时，会有不可打印的控制字符
 */
public class StringProcessor {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static String encodeString(String data, String key) {
        byte[] resultBytes = MyDES.encode(data.getBytes(StandardCharsets.UTF_8), key);
        return encoder.encodeToString(resultBytes);
    }

    public static String decodeString(String data, String key) {
        byte[] resultBytes = MyDES.decode(decoder.decode(data), key);
        if (resultBytes == null)
            return "";
        return new String(resultBytes, StandardCharsets.UTF_8);
    }

}
