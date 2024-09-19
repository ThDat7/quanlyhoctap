package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOutlineModel {
    String courseCode;
    String url;
}
