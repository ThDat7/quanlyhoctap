package com.thanhdat.quanlyhoctap.util;

import com.thanhdat.quanlyhoctap.entity.Semester;

public class YearHelper {
    public static String getYearStringBySemester(Semester semester) {
        return String.format("%s-%s", semester.getYear(), semester.getYear() + 1);
    }
}
