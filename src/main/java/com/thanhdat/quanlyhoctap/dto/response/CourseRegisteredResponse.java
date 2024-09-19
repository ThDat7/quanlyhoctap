package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRegisteredResponse {
    Integer id;
    String courseCode;
    String courseName;
    String studentClassName;
    Float courseCredits;
    LocalDateTime timeRegistered;
    String teacherCode;
    List<ScheduleStudyCourseRegisterResponse> scheduleStudies;
}
