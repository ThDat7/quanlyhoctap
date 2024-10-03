package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassResponse {
    Long id;
    String courseName;
    String courseCode;
    Float courseCredits;
    String studentClassName;
    String teacherName;
}
