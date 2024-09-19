package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.NewsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsController {
    NewsService newsService;

    @GetMapping("/view")
    public ResponseEntity<DataWithCounterDto<NewsResponse>> getAllNews(@RequestParam Map<String, String> params){
        return ResponseEntity.ok(newsService.getAll(params));
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<NewsViewResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(newsService.get(id));
    }


    @PostMapping
    public ResponseEntity<Void> create(@RequestBody NewsCrudRequest createRequest) {
        newsService.create(createRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsViewCrudResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody NewsCrudRequest updateRequest) {
        newsService.update(id, updateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<DataWithCounterDto<NewsCrudResponse>> getAllCrud(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(newsService.getAllCrud(params));
    }
}
