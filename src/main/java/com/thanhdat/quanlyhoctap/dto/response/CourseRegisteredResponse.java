package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegisteredResponse {
    private Integer id;
    private String courseCode;
    private String courseName;
    private String studentClassName;
    private Float courseCredits;
    private LocalDateTime timeRegistered;
    private String teacherCode;
    private List<ScheduleStudyCourseRegisterResponse> scheduleStudies;
}
