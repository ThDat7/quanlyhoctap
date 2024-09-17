package com.thanhdat.quanlyhoctap.dto.response;

import com.thanhdat.quanlyhoctap.entity.CourseType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseViewCrudResponse {
    private Integer id;
    private String name;

    private String code;
    private Float credits;
    private String majors;

    private CourseType type;

    private Integer sessionInWeek;
    private Integer theoryPeriod;
    private Integer practicePeriod;
}
