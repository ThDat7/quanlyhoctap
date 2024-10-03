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
public class CourseClassCrudRequest {
    @NotNull(message = "COURSE_NOT_NULL")
    Long courseId;
    @NotNull(message = "SEMESTER_NOT_NULL")
    Long semesterId;
    @NotNull(message = "TEACHER_NOT_NULL")
    Long teacherId;
    @NotNull(message = "STUDENT_CLASS_NOT_NULL")
    Long studentClassId;
    @NotNull(message = "COURSE_CLASS_CAPACITY_NOT_NULL")
    @Positive(message = "COURSE_CLASS_CAPACITY_POSITIVE")
    Integer capacity;
}
