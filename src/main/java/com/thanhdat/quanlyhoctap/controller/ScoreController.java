package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.MidtermExamScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.ScoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {
    ScoreService scoreService;

    @GetMapping("/course-class/{courseClassId}/current-teacher")
    public ResponseEntity<List<TeacherScoreResponse>> getScoreByCourseClassAndCurrentTeacher(@PathVariable Integer courseClassId) {
        return ResponseEntity.ok(scoreService.getByCourseClassAndCurrentTeacher(courseClassId));
    }

    @PostMapping("/update/current-teacher")
    public ResponseEntity updateScoreByCurrentTeacher(@RequestBody List<MidtermExamScoreUpdateRequest> midtermExamScoreUpdateRequests) {
        scoreService.updateByCurrentTeacher(midtermExamScoreUpdateRequests);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}