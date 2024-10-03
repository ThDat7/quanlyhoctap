package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleStudyRequest {
    Long id;
    @NotNull(message = "SCHEDULE_STUDY_WEEK_LENGTH_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_WEEK_LENGTH_MIN")
    Integer weekLength;
    @NotNull(message = "SCHEDULE_STUDY_START_DATE_NOT_NULL")
    LocalDate startDate;
    @NotNull(message = "SCHEDULE_STUDY_SHIFT_START_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_SHIFT_START_MIN")
    Integer shiftStart;
    @NotNull(message = "SCHEDULE_STUDY_SHIFT_LENGTH_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_SHIFT_LENGTH_MIN")
    Integer shiftLength;
    @NotNull(message = "SCHEDULE_STUDY_CLASSROOM_ID_NOT_NULL")
    Long roomId;
}
