package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;
import com.thanhdat.quanlyhoctap.service.StudentStatusService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student-status")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentStatusController {
    StudentStatusService studentStatusService;

    @GetMapping("/current-student")
    public ResponseEntity<List<StudentStatusResponse>> getCurrentStudentStatus(){
        return ResponseEntity.ok(studentStatusService.getByCurrentStudent());
    }
}
