package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudentStatusResponse;
import com.thanhdat.quanlyhoctap.service.StudentStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student-status")
@AllArgsConstructor
public class StudentStatusController {
    private final StudentStatusService studentStatusService;

    @GetMapping("/current-student")
    public ResponseEntity<List<StudentStatusResponse>> getCurrentStudentStatus(){
        return ResponseEntity.ok(studentStatusService.getByCurrentStudent());
    }
}
