package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeToUseClassroomRequest {
    LocalDateTime startTime;
    LocalDateTime endTime;
}
