package com.arinoyu.controller;

import com.arinoyu.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Slf4j
public class UploadController {

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            log.info("上传的文件是空的");
        }
        // 文件名
        String fileName = file.getOriginalFilename();

        // 文件保存路径
        String filesPath = Utils.getFilesPath();
        String savePath = filesPath + "/" + fileName;
        log.info("文件保存地址：" + savePath);

        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            if (saveFile.mkdirs()) {
                log.info("文件夹不存在，已创建文件夹");
            }
        }

        try {
            // 将临时存储的文件移动到真实存储路径下
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "文件上传成功";
    }

}
