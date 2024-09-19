package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassScheduleResponse {
    Long id;
    String courseName;
    String courseCode;
    Float courseCredits;
    String studentClassName;
    String teacherName;
    List<ScheduleStudyTimeTableResponse> schedules;
}

