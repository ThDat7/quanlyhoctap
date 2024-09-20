package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.service.SemesterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterController {
    SemesterService semesterService;

    @GetMapping("/current-student")
    public ApiResponse<List<SemesterDetailResponse>> getCurrentStudentSemesters(){
        return ApiResponse.ok(semesterService.getByCurrentStudent());
    }
    @GetMapping("none-locked")
    public ApiResponse<List<SemesterDetailResponse>> getNoneLockedSemesters(){
        return ApiResponse.ok(semesterService.getNoneLocked());
    }
    @GetMapping("/current-teacher")
    public ApiResponse<List<SemesterDetailResponse>> getCurrentTeacherSemesters(){
        return ApiResponse.ok(semesterService.getByCurrentTeacher());
    }
}
