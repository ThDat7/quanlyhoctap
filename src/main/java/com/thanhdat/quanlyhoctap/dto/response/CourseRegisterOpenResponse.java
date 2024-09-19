package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseRegisterOpenResponse {
    Integer id;
    String courseCode;
    String courseName;
//    String groupName; ???
    Float courseCredits;
    String studentClassName;
    Integer capacity;
    Integer remaining;
    String teacherCode;
    List<ScheduleStudyCourseRegisterResponse> scheduleStudies;
}
