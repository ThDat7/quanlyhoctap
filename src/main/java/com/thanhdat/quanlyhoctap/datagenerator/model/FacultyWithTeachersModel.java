package com.thanhdat.quanlyhoctap.datagenerator.model;

import lombok.Data;

import java.util.List;

@Data
public class FacultyWithTeachersModel {
    private String name;
    private String alias;
    private List<MajorModel> majors;
    private List<String> teachers;
}