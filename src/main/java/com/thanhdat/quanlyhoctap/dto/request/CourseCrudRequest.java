package com.thanhdat.quanlyhoctap.dto.request;

import com.thanhdat.quanlyhoctap.entity.CourseType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseCrudRequest {
    private String name;
    private String code;
    private Float credits;
    private CourseType type;

    private Integer sessionInWeek;
    private Integer theoryPeriod;
    private Integer practicePeriod;
}
