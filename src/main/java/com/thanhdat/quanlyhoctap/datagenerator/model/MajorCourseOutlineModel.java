package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorCourseOutlineModel {
    String majorName;
    List<CourseOutlineModel> outlines;
}
