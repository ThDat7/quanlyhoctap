package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseClassTeachingResponse {
    private Integer id;
    private String courseName;
//    private String code; (courseClassCode)
    private String courseCode;
}
