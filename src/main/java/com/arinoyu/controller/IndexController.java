package com.arinoyu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class IndexController {

    /**
     * 这里使用了Thymeleaf模版引擎，解析index为html文件并返回
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
