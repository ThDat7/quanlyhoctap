package com.thanhdat.quanlyhoctap.service;

import com.thanhdat.quanlyhoctap.dto.request.MidtermExamScoreUpdateRequest;
import com.thanhdat.quanlyhoctap.dto.response.TeacherScoreResponse;

import java.util.List;

public interface ScoreService {
    List<TeacherScoreResponse> getByCourseClassAndCurrentTeacher(Integer courseClassId);

    void updateByCurrentTeacher(List<MidtermExamScoreUpdateRequest> midtermExamScoreUpdateRequests);
}
