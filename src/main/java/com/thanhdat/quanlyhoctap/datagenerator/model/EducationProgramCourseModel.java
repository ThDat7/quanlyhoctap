package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourseModel {
    Integer semester;
    Integer year;
    Set<String> coursesCode;
}