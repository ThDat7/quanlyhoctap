package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EducationProgramCourseRequest {
    private Integer id;
    private Integer semester;
    private Integer courseId;
}
