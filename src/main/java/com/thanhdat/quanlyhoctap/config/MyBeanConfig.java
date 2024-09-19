package com.thanhdat.quanlyhoctap.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:config.yml")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyBeanConfig {
    Environment env;

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

    @Bean
    public Faker faker(){
        return new Faker();
    }
}
