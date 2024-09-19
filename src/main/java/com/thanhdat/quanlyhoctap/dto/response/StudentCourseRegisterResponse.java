package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCourseRegisterResponse {
    Integer semester;
    String year;
    List<CourseRegisterOpenResponse> openCourses;
    List<CourseRegisteredResponse> registeredCourses;
}
