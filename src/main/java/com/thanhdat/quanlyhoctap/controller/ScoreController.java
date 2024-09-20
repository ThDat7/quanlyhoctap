package com.thanhdat.quanlyhoctap.controller;

import com.thanhdat.quanlyhoctap.dto.request.ScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.*;
import com.thanhdat.quanlyhoctap.service.ScoreService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {
    ScoreService scoreService;

    @GetMapping("/course-class/{courseClassId}/current-teacher")
    public ApiResponse<List<ScoreResponse>> getScoreByCourseClassAndCurrentTeacher(@PathVariable Long courseClassId) {
        return ApiResponse.ok(scoreService.getByCourseClassAndCurrentTeacher(courseClassId));
    }

    @PostMapping("/update/current-teacher")
    public ApiResponse<Void> updateScoreByCurrentTeacher(@RequestBody @Valid List<ScoreUpdateRequest> scoreUpdateRequests) {
        scoreService.updateByCurrentTeacher(scoreUpdateRequests);
        return ApiResponse.ok();
    }
}