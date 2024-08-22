package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.Data;

@Data
public class CourseModel {
    private String name;
    private String code;
    private Float credits;

    private Integer sessionInWeek;
    private Integer theoryPeriod;
    private Integer practicePeriod;
}
