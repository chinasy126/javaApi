package com.example.java.config;

import com.example.java.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.image.prefix}")
    private String upImagePrefix;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new LoginInterceptor()).excludePathPatterns("/user/login");
        //registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**");

        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").
                excludePathPatterns("/user/login", "/multipleImageUpload").
                excludePathPatterns("/user/verificationcode").
                excludePathPatterns("/user/logout").
                excludePathPatterns("/" + upImagePrefix + "/**").
                excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**").
                excludePathPatterns("/statics/**");

    }


}
