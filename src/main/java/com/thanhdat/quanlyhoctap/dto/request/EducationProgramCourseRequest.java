package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseRequest {
    Integer id;
    Integer semester;
    Integer courseId;
}
