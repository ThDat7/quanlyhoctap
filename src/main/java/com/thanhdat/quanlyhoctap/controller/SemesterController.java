package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.SemesterDetailResponse;
import com.thanhdat.quanlyhoctap.service.SemesterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SemesterDetailResponse>> getCurrentStudentSemesters(){
        return ResponseEntity.ok(semesterService.getByCurrentStudent());
    }
    @GetMapping("none-locked")
    public ResponseEntity<List<SemesterDetailResponse>> getNoneLockedSemesters(){
        return ResponseEntity.ok(semesterService.getNoneLocked());
    }
    @GetMapping("/current-teacher")
    public ResponseEntity<List<SemesterDetailResponse>> getCurrentTeacherSemesters(){
        return ResponseEntity.ok(semesterService.getByCurrentTeacher());
    }
}
