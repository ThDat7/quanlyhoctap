package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseDto {
    Long courseOutlineId;
    String courseName;
    Integer semester;
    String courseOutlineUrl;
}