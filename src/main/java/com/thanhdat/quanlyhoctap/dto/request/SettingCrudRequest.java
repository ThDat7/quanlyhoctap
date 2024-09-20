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
public class
SettingCrudRequest {
    @NotNull(message = "SETTING_KEY_NOT_NULL")
    @Size(min = 1, max = 255, message = "SETTING_KEY_SIZE")
    String key;
    @NotNull(message = "SETTING_VALUE_NOT_NULL")
    @Size(min = 1, max = 255, message = "SETTING_VALUE_SIZE")
    String value;
}
