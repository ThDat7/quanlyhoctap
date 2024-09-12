package com.thanhdat.quanlyhoctap.dto.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidtermExamScoreUpdateRequest {
    private Integer studyId;
    private Float score;
}
