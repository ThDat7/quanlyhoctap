package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MajorCrudRequest {
    @NotNull(message = "MAJOR_NAME_NOT_NULL")
    @Size(min = 3, max = 255, message = "MAJOR_NAME_SIZE")
    String name;
    @NotNull(message = "MAJOR_ALIAS_NOT_NULL")
    @Size(min = 2, max = 100, message = "MAJOR_ALIAS_SIZE")
    String alias;
    @NotNull(message = "FACULTY_NOT_NULL")
    Long facultyId;

    @NotNull(message = "MAJOR_SPECIALIZE_TUITION_NOT_NULL")
    @Positive(message = "MAJOR_SPECIALIZE_TUITION_POSITIVE")
    Integer specializeTuition;
    @NotNull(message = "MAJOR_GENERAL_TUITION_NOT_NULL")
    @Positive(message = "MAJOR_GENERAL_TUITION_POSITIVE")
    Integer generalTuition;
}
