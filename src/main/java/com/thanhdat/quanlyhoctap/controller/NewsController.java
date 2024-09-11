package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.NewsResponse;
import com.thanhdat.quanlyhoctap.dto.response.NewsViewResponse;
import com.thanhdat.quanlyhoctap.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<DataWithCounterDto<NewsResponse>> getAllNews(@RequestParam Map<String, String> params){
        return ResponseEntity.ok(newsService.getAll(params));
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<NewsViewResponse> get(@PathVariable Integer id){
        return ResponseEntity.ok(newsService.get(id));
    }
}
