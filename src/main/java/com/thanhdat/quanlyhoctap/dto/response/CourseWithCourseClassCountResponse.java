package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseWithCourseClassCountResponse {
    Long id;
    String name;
    Float credit;
    Long needCount;
    Long presentCount;
}
