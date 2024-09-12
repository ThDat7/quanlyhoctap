package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOutlineViewTeacherResponse {
    private Integer id;
    private String courseName;
    private String courseCode;
    private String url;
    private CourseRuleResponse courseRule;
}
