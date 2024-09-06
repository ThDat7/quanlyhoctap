package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationProgramSearchDto {
    private int id;
    private String majorName;
    private int schoolYear;
}