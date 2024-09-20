package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.ScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.ScoreResponse;

import java.util.List;

public interface ScoreService {
    List<ScoreResponse> getByCourseClassAndCurrentTeacher(Long courseClassId);

    void updateByCurrentTeacher(List<ScoreUpdateRequest> scoreUpdateRequests);
}
