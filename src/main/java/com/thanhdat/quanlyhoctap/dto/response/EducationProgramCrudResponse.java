package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramCrudResponse {
    private Integer id;
    private String majorName;
    private Integer schoolYear;
    private Integer numberOfCourses;
}
