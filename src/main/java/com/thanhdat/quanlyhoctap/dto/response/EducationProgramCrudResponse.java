package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCrudResponse {
    Long id;
    String majorName;
    Integer schoolYear;
    Integer numberOfCourses;
}
