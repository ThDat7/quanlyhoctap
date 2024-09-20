package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.MajorCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.MajorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MajorController {
    MajorService majorService;

    @GetMapping
    public ApiResponse<DataWithCounterDto<MajorCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(majorService.getAll(params));
    }

    @GetMapping("/{id}")
    public ApiResponse<MajorViewCrudResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(majorService.getById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@RequestBody @Valid MajorCrudRequest majorCrudRequest) {
        majorService.create(majorCrudRequest);
        return ApiResponse.ok();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody @Valid MajorCrudRequest majorCrudRequest) {
        majorService.update(id, majorCrudRequest);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        majorService.delete(id);
        return ApiResponse.ok();
    }

    @GetMapping("/select-options")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptions() {
        return ApiResponse.ok(majorService.getSelectOptions());
    }
}
