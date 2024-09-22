package com.thanhdat.quanlyhoctap.util;

import com.thanhdat.quanlyhoctap.entity.EducationProgram;
import com.thanhdat.quanlyhoctap.entity.Semester;

public class SemesterCalculator {
    public static Integer calculateSemesterInEP(EducationProgram ep, Semester semester) {
        if (semester == null)
            return null;

        int countSchoolYear = semester.getYear() - ep.getSchoolYear();
        return countSchoolYear * 3 + semester.getSemester();
    }

    public static Integer calculateSemesterInYear(Integer semesterInEP) {
        int maxSemesterInYear = 3;
        int moduleSemester = semesterInEP % maxSemesterInYear;
        boolean isLastSemester = moduleSemester == 0;

        if (isLastSemester)
            return maxSemesterInYear;
        return moduleSemester;
    }

    public static Integer calculateYearFromEP(Integer semesterInEP) {
        int maxSemesterInYear = 3;
        int moduleSemester = semesterInEP % maxSemesterInYear;
        boolean isLastSemester = moduleSemester == 0;

        if (isLastSemester)
            return semesterInEP / maxSemesterInYear - 1;
        return semesterInEP / maxSemesterInYear;
    }
}