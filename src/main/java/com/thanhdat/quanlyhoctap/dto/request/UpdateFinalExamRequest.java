package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFinalExamRequest {
    @NotNull(message = "")
    LocalDateTime time;
    @NotNull(message = "")
    Long roomId;
}
