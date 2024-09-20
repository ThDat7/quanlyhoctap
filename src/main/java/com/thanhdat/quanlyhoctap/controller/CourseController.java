package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.CourseCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.CourseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {
    CourseService courseService;

    @PostMapping
    public ApiResponse<Void> create(@RequestBody CourseCrudRequest createRequest) {
        courseService.create(createRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody CourseCrudRequest updateRequest) {
        courseService.update(id, updateRequest);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<DataWithCounterDto<CourseCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(courseService.getAll(params));
    }

    @GetMapping("/types")
    public ApiResponse<List<SelectOptionResponse>> getTypes() {
        return ApiResponse.ok(courseService.getTypes());
    }

    @GetMapping("/select-options")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptions() {
        return ApiResponse.ok(courseService.getSelectOptions());
    }
}
