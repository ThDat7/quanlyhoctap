package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.SelectOptionResponse;
import com.thanhdat.quanlyhoctap.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staffs")
@AllArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @GetMapping("/select-options")
    public ResponseEntity<List<SelectOptionResponse>> getSelectOptions(){
        return ResponseEntity.ok(staffService.getSelectOptions());
    }
}
