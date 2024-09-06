package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramCourseDto {
    private Integer courseOutlineId;
    private String courseName;
    private Integer semester;
    private String courseOutlineUrl;
}