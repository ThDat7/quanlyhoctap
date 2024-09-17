package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramCourseResponse {
    private Integer id;
    private Integer courseOutlineId;
    private Integer courseId;
    private Integer semester;
    private String courseOutlineUrl;
}
