package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramViewDto {
    Long id;
    String majorName;
    Integer schoolYear;
    List<EducationProgramCourseDto> educationProgramCourses;
}
