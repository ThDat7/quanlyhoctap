package com.thanhdat.quanlyhoctap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class MyBeanConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("dd/MM/yyyy");
    }
}
