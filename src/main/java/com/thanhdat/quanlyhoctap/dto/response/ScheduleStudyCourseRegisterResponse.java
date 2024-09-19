package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleStudyCourseRegisterResponse {
    LocalDate startDate;
    Integer weekLength;
    String roomName;
    Integer shiftStart;
    Integer shiftLength;
}
