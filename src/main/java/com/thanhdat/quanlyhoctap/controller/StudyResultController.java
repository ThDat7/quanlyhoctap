package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.response.StudyResultSemesterResponse;
import com.thanhdat.quanlyhoctap.service.StudyResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/study-results")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudyResultController {
    StudyResultService studyResultService;

    @GetMapping("/current-student")
    public ResponseEntity<List<StudyResultSemesterResponse>> getByCurrentStudent() {
        return ResponseEntity.ok(studyResultService.getByCurrentStudent());
    }
}
