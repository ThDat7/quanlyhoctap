package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseOutlineSearchDto {
    private int id;
    private String courseName;
    private String teacherName;
    private Float courseCredits;
    private List<Integer> years;
    private String url;
}
