package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.service.ExamService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final StudentService studentService;
    private final CourseClassRepository courseClassService;

    @Override
    public List<ExamScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId) {
        int currentStudentId = studentService.getCurrentStudentId();
        return courseClassService.findBySemesterIdAndStudentId(semesterId, currentStudentId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private ExamScheduleResponse convertToResponse(CourseClass courseClass){
        return ExamScheduleResponse.builder()
                .courseCode(courseClass.getCourse().getCode())
                .courseName(courseClass.getCourse().getName())
                .studentClassName(courseClass.getStudentClass().getName())
                .quantityStudent((int) courseClass.getStudies().stream().count())
                .startTime(courseClass.getFinalExam().getStartTime())
                .roomName(courseClass.getFinalExam().getClassroom().getName())
                .build();
    }
}
