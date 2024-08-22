package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.datagenerator.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexController {

    @Autowired
    private DataGenerator dataGenerator;

    @GetMapping
    public String test() {
        return "Ok";
    }

    @GetMapping("/data")
    public String generateData() throws Exception {
        dataGenerator.generateData();
        return "Data generated";
    }
}
