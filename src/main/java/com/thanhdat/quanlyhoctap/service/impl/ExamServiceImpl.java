package com.thanhdat.quanlyhoctap.service.impl;

import com.thanhdat.quanlyhoctap.dto.response.ExamScheduleResponse;
import com.thanhdat.quanlyhoctap.entity.CourseClass;
import com.thanhdat.quanlyhoctap.repository.CourseClassRepository;
import com.thanhdat.quanlyhoctap.service.ExamService;
import com.thanhdat.quanlyhoctap.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExamServiceImpl implements ExamService {
    private final StudentService studentService;
    private final CourseClassRepository courseClassRepository;

    @Override
    public List<ExamScheduleResponse> getByCurrentStudentAndSemester(Integer semesterId) {
        int currentStudentId = studentService.getCurrentStudentId();
        return courseClassRepository.findBySemesterIdAndStudentId(semesterId, currentStudentId)
                .stream()
                .flatMap(e -> convertToResponse(e).stream())
                .sorted(Comparator.comparing(ExamScheduleResponse::getStartTime))
                .toList();
    }

    private List<ExamScheduleResponse> convertToResponse(CourseClass courseClass){
        return courseClass.getExams().stream()
                .map(exam -> ExamScheduleResponse.builder()
                .courseCode(courseClass.getCourse().getCode())
                .courseName(courseClass.getCourse().getName())
                .studentClassName(courseClass.getStudentClass().getName())
                .quantityStudent((int) courseClass.getStudies().stream().count())
                .startTime(exam.getStartTime())
                .roomName(exam.getClassroom().getName())
                .type(exam.getType())
                .build())
        .collect(Collectors.toList());
    }
}
