package com.thanhdat.quanlyhoctap.dto.response;

import com.thanhdat.quanlyhoctap.entity.CourseType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseViewCrudResponse {
    Integer id;
    String name;

    String code;
    Float credits;
    String majors;

    CourseType type;

    Integer sessionInWeek;
    Integer theoryPeriod;
    Integer practicePeriod;
}
