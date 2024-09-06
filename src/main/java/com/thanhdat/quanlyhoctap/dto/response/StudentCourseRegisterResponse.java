package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseRegisterResponse {
    private Integer semester;
    private String year;
    List<CourseRegisterOpenResponse> openCourses;
    List<CourseRegisteredResponse> registeredCourses;
}
