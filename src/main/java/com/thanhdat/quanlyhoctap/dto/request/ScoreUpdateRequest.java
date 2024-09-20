package com.thanhdat.quanlyhoctap.dto.request;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreUpdateRequest {
    @NotNull(message = "STUDY_NOT_NULL")
    Long studyId;
    @DecimalMin(value = "0.0", message = "SCORE_MIN_VALUE")
    @DecimalMax(value = "10.0", message = "SCORE_MAX_VALUE")
    Float score;
}
