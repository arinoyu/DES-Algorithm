package com.arinoyu.config;

import com.arinoyu.util.Utils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
          资源映射路径
          addResourceHandler：访问映射路径
          addResourceLocations：资源绝对路径
         */
        String filesPath = Utils.getFilesPath();
        String resourceLocation = "file:" + filesPath + "/";
        registry.addResourceHandler("/**").addResourceLocations(resourceLocation);
    }
}