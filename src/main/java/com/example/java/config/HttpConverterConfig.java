package com.example.java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HttpConverterConfig implements WebMvcConfigurer {
<<<<<<< HEAD
    public final static String IMG_PATH_PREFIX = "statics/upload/imgs";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + fileDirPath + "/");
=======
    //public final static String IMG_PATH_PREFIX = "statics/upload/imgs";
    public final static String IMG_PATH_PREFIX = "public";

    public final static String IMG_PATH = "src/main/resources/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileDirPath = new String(IMG_PATH + IMG_PATH_PREFIX);
        registry.addResourceHandler("/public/**").
                addResourceLocations("file:" + fileDirPath + "/");
>>>>>>> master

        // "classpath:/static/"
    }

}
