package com.example.java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HttpConverterConfig implements WebMvcConfigurer {
    public final static String IMG_PATH_PREFIX = "statics/upload/imgs";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + fileDirPath + "/");

        // "classpath:/static/"
    }

}
