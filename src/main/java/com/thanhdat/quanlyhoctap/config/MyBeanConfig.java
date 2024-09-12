package com.thanhdat.quanlyhoctap.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:config.yml")
@AllArgsConstructor
public class MyBeanConfig {
    private Environment env;

    @Bean
    public Cloudinary cloudinary() {
        Map config = new HashMap<>();
        config.put("cloud_name", this.env.getProperty("cloud_name"));
        config.put("api_key", this.env.getProperty("api_id"));
        config.put("api_secret", this.env.getProperty("api_secret"));
        config.put("secure", this.env.getProperty("secure"));
        return new Cloudinary(config);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
