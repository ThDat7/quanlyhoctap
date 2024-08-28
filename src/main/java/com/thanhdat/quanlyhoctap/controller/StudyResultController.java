package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;
import com.thanhdat.quanlyhoctap.service.StudyResultService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study-results")
@AllArgsConstructor
public class StudyResultController {
    private final StudyResultService studyResultService;

    @GetMapping("/current-student")
    public ResponseEntity<List<StudyResultSemesterResponse>> getByCurrentStudent() {
        return ResponseEntity.ok(studyResultService.getByCurrentStudent());
    }
}
