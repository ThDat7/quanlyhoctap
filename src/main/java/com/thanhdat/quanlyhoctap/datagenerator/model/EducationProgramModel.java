package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramModel {
    String major;
    Integer schoolYear;
    Set<EducationProgramCourseModel> educationProgramCourse;
}
