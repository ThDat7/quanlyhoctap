package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseResponse {
    Long id;
    Long courseOutlineId;
    Long courseId;
    Integer semester;
    String courseOutlineUrl;
}
