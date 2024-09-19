package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.entity.CourseType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseCrudRequest {
    String name;
    String code;
    Float credits;
    CourseType type;

    Integer sessionInWeek;
    Integer theoryPeriod;
    Integer practicePeriod;
}
