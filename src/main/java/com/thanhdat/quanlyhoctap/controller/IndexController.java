package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.datagenerator.DataGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndexController {
    DataGenerator dataGenerator;

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
