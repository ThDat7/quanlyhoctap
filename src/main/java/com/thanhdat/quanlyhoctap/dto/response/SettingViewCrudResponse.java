package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingViewCrudResponse {
    private String key;
    private String value;
}