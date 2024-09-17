package com.thanhdat.quanlyhoctap.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacultyCrudRequest {
    private String name;
    private String alias;
}
