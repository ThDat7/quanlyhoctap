package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramViewCrudResponse {
    private Integer id;
    private Integer schoolYear;

    private Integer majorId;

    private List<EducationProgramCourseResponse> educationProgramCourses;
}
