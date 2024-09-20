package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.SettingCrudRequest;
import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.DataWithCounterDto;
import com.thanhdat.quanlyhoctap.dto.response.SettingCrudResponse;
import com.thanhdat.quanlyhoctap.dto.response.SettingViewCrudResponse;
import com.thanhdat.quanlyhoctap.service.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingController {
    SettingService settingService;

    @GetMapping
    public ApiResponse<DataWithCounterDto<SettingCrudResponse>> getAll(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(settingService.getAll(params));
    }

    @PutMapping("/{key}")
    public ApiResponse<Void> update(@PathVariable String key, @RequestBody SettingCrudRequest updateRequest) {
        settingService.update(key, updateRequest);
        return ApiResponse.ok();
    }

    @GetMapping("/{key}")
    public ApiResponse<SettingViewCrudResponse> getById(@PathVariable String key) {
        return ApiResponse.ok(settingService.getById(key));
    }
}
