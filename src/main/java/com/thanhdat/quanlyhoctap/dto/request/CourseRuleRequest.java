package com.thanhdat.quanlyhoctap.dto.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRuleRequest {
    private Float midTermFactor;
    private Float finalTermFactor;
    private Float passScore;
}
