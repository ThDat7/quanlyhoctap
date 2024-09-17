package com.thanhdat.quanlyhoctap.dto.response;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterDetailResponse {
    private Integer id;
    private String year;
    private Integer semester;
    private LocalDate startDate;
    private Integer durationWeeks;
}
