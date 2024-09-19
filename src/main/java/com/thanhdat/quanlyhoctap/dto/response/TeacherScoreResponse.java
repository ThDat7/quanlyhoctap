package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherScoreResponse {
    private Long studyId;
    private String studentCode;
    private String studentLastName;
    private String studentFirstName;
    private Float score;
}
