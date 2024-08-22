package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.Data;

import java.util.List;

@Data
public class MajorCourseOutlineModel {
    private String majorName;
    private List<CourseOutlineModel> outlines;
}
