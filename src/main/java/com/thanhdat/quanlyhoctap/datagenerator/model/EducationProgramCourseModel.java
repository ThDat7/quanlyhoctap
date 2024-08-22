package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.Data;

import java.util.Set;

@Data
public class EducationProgramCourseModel {
    private Integer semester;
    private Set<String> coursesCode;
}