package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeToUseClassroomRequest {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
