package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {
    StaffService staffService;

    @GetMapping("/select-options")
    public ApiResponse<List<SelectOptionResponse>> getSelectOptions(){
        return ApiResponse.ok(staffService.getSelectOptions());
    }
}
