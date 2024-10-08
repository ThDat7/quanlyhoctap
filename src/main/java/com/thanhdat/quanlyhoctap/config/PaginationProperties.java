package com.thanhdat.quanlyhoctap.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class PaginationProperties {
    @Value( "${pagination.default.page-size}" )
    private int pageSize;
}
