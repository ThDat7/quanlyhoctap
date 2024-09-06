package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStudyCourseRegisterResponse {
    private LocalDate startDate;
    private Integer weekLength;
    private String roomName;
    private Integer shiftStart;
    private Integer shiftLength;
}
