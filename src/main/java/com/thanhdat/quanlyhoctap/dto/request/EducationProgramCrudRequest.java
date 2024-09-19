package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.dto.response.EducationProgramCourseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCrudRequest {
    int schoolYear;
    Integer majorId;
    List<EducationProgramCourseRequest> educationProgramCourses;
}
