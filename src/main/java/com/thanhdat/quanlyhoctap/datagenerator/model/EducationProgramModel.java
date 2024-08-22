package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.Data;

import java.util.Set;
@Data
public class EducationProgramModel {
    private String major;
    private Integer schoolYear;
    private Set<EducationProgramCourseModel> educationProgramCourse;
}
