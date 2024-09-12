package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRuleResponse {
    private Float midTermFactor;
    private Float finalTermFactor;
    private Float passScore;
}
