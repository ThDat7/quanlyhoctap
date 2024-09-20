package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCrudRequest {
    @NotNull(message = "EDUCATION_PROGRAM_SCHOOL_YEAR_NOT_NULL")
    @Min(value = 2000, message = "EDUCATION_PROGRAM_SCHOOL_YEAR_MIN_VALUE")
    int schoolYear;
    @NotNull(message = "MAJOR_NOT_NULL")
    Long majorId;
    @Valid
    List<EducationProgramCourseRequest> educationProgramCourses;
}
