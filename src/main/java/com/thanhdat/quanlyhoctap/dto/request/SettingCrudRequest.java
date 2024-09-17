package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class
SettingCrudRequest {
    private String key;
    private String value;
}
