package com.thanhdat.quanlyhoctap.dto.request;


import com.thanhdat.quanlyhoctap.entity.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleStudyClassroomAvailableRequest {
    @NotNull(message = "SCHEDULE_STUDY_ROOM_TYPE_NOT_NULL")
    RoomType roomType;
    @NotNull(message = "SCHEDULE_STUDY_START_DATE_NOT_NULL")
    LocalDate startDate;
    @NotNull(message = "SCHEDULE_STUDY_WEEK_LENGTH_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_WEEK_LENGTH_MIN")
    Integer weekLength;
    @NotNull(message = "SCHEDULE_STUDY_SHIFT_START_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_SHIFT_START_MIN")
    Integer shiftStart;
    @NotNull(message = "SCHEDULE_STUDY_SHIFT_LENGTH_NOT_NULL")
    @Min(value = 1, message = "SCHEDULE_STUDY_SHIFT_LENGTH_MIN")
    Integer shiftLength;
}
