package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseRequest {
    Long id;
    @NotNull(message = "SEMESTER_NOT_NULL")
    @Positive(message = "SEMESTER_POSITIVE")
    Integer semester;
    @NotNull(message = "COURSE_NOT_NULL")
    Long courseId;
}
