package com.thanhdat.quanlyhoctap.dto.request;


import com.thanhdat.quanlyhoctap.validator.constraint.SumMidFinalTermFactor;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@SumMidFinalTermFactor(midTermFactorField = "midTermFactor", finalTermFactorField = "finalTermFactor",
        message = "SUM_MID_FINAL_TERM_FACTOR")
public class CourseRuleRequest {
    @NotNull(message = "MID_TERM_FACTOR_NOT_NULL")
    @DecimalMin(value = "0.3", message = "MID_TERM_FACTOR_MIN_VALUE")
    Float midTermFactor;
    @NotNull(message = "FINAL_TERM_FACTOR_NOT_NULL")
    @DecimalMin(value = "0.5", message = "FINAL_TERM_FACTOR_MIN_VALUE")
    Float finalTermFactor;
    @NotNull(message = "PASS_SCORE_NOT_NULL")
    @DecimalMin(value = "4.0", message = "PASS_SCORE_MIN_VALUE")
    @DecimalMax(value = "5.0", message = "PASS_SCORE_MAX_VALUE")
    Float passScore;
}
