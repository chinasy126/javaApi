package com.example.java.config;

import org.dromara.easyes.starter.config.EasyEsConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EasyEsConfig {

    @Bean
    public EasyEsConfigProperties easyEsConfigProperties(){
        return new EasyEsConfigProperties();
    }
}
