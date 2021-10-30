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
public class EncodeController {

    @PostMapping("/encodeFile")
    @ResponseBody
    public String encodeFile(@RequestBody Map<String, String> map) {
        String key = map.get("key");
        String fileName = map.get("fileName");
        encodeAndSave(key, fileName);
        return "encoded-" + fileName;
    }

    /**
     * 该方法完成了加密文件并保存的过程
     */
    private void encodeAndSave(String key, String fileName) {
        String filesPath = Utils.getFilesPath();
        String filePath = filesPath + "/" + fileName;

        String encodedFilePath = filesPath + "/encoded-" + fileName;
        FileProcessor.encodeFile(filePath, encodedFilePath, key);
    }

    @PostMapping("/encodeText")
    @ResponseBody
    public String encodeText(@RequestBody Map<String, String> map) {
        String key = map.get("key");
        String plainText = map.get("plainText");
        return StringProcessor.encodeString(plainText, key);
    }

}
