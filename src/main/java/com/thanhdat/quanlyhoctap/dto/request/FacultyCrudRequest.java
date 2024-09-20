package com.thanhdat.quanlyhoctap.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FacultyCrudRequest {
    @NotNull(message = "FACULTY_NAME_NOT_NULL")
    @Size(min = 3, max = 255, message = "FACULTY_NAME_SIZE")
    String name;
    @NotNull(message = "FACULTY_ALIAS_NOT_NULL")
    @Size(min = 2, max = 100, message = "FACULTY_ALIAS_SIZE")
    String alias;
}
