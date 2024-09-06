package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramViewDto {
    private Integer id;
    private String majorName;
    private Integer schoolYear;
    private List<EducationProgramCourseDto> educationProgramCourses;
}
