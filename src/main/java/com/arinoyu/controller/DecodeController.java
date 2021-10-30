package com.arinoyu.controller;

import com.arinoyu.des.FileProcessor;
import com.arinoyu.des.StringProcessor;
import com.arinoyu.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Slf4j
public class DecodeController {

    @PostMapping("/decodeFile")
    @ResponseBody
    public String decodeFile(@RequestBody Map<String, String> map) {
        String key = map.get("key");
        String fileName = map.get("fileName");
        decodeAndSave(key, fileName);
        return fileName.replace("encoded-", "");
    }

    /**
     * 该方法完成了解密文件并保存的过程
     */
    private void decodeAndSave(String key, String fileName) {
        String filesPath = Utils.getFilesPath();
        String filePath = filesPath + "/" + fileName;

        String decodedFilePath = filesPath + "/" + fileName.replace("encoded-", "");
        FileProcessor.decodeFile(filePath, decodedFilePath, key);
    }

    @PostMapping("/decodeText")
    @ResponseBody
    public String decodeText(@RequestBody Map<String, String> map) {
        String key = map.get("key");
        String encipherText = map.get("encipherText");
        return StringProcessor.decodeString(encipherText, key);
    }
}