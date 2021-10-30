package com.arinoyu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DesAlgorithmApplication {

    public static void main(String[] args) {

        // 运行整个SpringBoot项目
        SpringApplication.run(DesAlgorithmApplication.class, args);
        log.info("项目运行地址：http://localhost:8080");

        // 运行项目之后自动打开浏览器
        openBrowser("http://localhost:8080");

    }

    private static void openBrowser(String url) {
        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");

        // 预先准备好命令字符串
        String command;
        if (osName.startsWith("Mac OS")) {
            // Mac的打开方式
            command = "open ";
        } else if (osName.startsWith("Windows")) {
            // Windows的打开方式
            command = "cmd /c start ";
        } else {
            // 如果系统是Unix or Linux，需要手动打开
            log.info("请手动打开浏览器并访问链接: http://localhost:8080");
            return;
        }
        Runtime run = Runtime.getRuntime();
        try {
            run.exec(command + url);
            log.info("启动浏览器打开项目成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}
