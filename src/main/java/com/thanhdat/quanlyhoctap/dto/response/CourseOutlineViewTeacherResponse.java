package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOutlineViewTeacherResponse {
    Integer id;
    String courseName;
    String courseCode;
    String url;
    CourseRuleResponse courseRule;
}
