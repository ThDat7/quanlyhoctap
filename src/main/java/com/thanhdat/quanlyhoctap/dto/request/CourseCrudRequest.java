package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.entity.CourseType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCrudRequest {
    @NotNull(message = "COURSE_NAME_NOT_NULL")
    @Size(min = 2, max = 255, message = "COURSE_NAME_SIZE")
    String name;
    @NotNull(message = "COURSE_CODE_NOT_NULL")
    @Size(min = 2, max = 255, message = "COURSE_CODE_SIZE")
    String code;
    @NotNull(message = "COURSE_CREDITS_NOT_NULL")
    @DecimalMin(value = "0.5", message = "COURSE_CREDITS_MIN_VALUE")
    Float credits;
    @NotNull(message = "COURSE_TYPE_NOT_NULL")
    CourseType type;
    @NotNull(message = "COURSE_SESSION_IN_WEEK_NOT_NULL")
    @Min(value = 0, message = "COURSE_SESSION_IN_WEEK_MIN_VALUE")
    Integer sessionInWeek;
    @NotNull(message = "COURSE_THEORY_PERIOD_NOT_NULL")
    @Min(value = 0, message = "COURSE_THEORY_PERIOD_MIN_VALUE")
    Integer theoryPeriod;
    @NotNull(message = "COURSE_PRACTICE_PERIOD_NOT_NULL")
    @Min(value = 0, message = "COURSE_PRACTICE_PERIOD_MIN_VALUE")
    Integer practicePeriod;
}
