package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseResponse {
    Integer id;
    Integer courseOutlineId;
    Integer courseId;
    Integer semester;
    String courseOutlineUrl;
}
