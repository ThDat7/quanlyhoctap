package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseModel {
    String name;
    String code;
    Float credits;
    String type;

    Integer sessionInWeek;
    Integer theoryPeriod;
    Integer practicePeriod;
}
