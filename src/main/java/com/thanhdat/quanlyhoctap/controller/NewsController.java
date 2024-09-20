package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.NewsCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.NewsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsController {
    NewsService newsService;

    @GetMapping("/view")
    public ApiResponse<DataWithCounterDto<NewsResponse>> getAllNews(@RequestParam Map<String, String> params){
        return ApiResponse.ok(newsService.getAll(params));
    }

    @GetMapping("/view/{id}")
    public ApiResponse<NewsViewResponse> get(@PathVariable Long id){
        return ApiResponse.ok(newsService.get(id));
    }


    @PostMapping
    public ApiResponse<Void> create(@RequestBody NewsCrudRequest createRequest) {
        newsService.create(createRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        newsService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<NewsViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(newsService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody NewsCrudRequest updateRequest) {
        newsService.update(id, updateRequest);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<DataWithCounterDto<NewsCrudResponse>> getAllCrud(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(newsService.getAllCrud(params));
    }
}
