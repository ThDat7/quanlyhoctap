package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramViewCrudResponse {
    Long id;
    Integer schoolYear;

    Long majorId;

    List<EducationProgramCourseResponse> educationProgramCourses;
}
