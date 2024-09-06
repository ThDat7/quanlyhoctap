package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataWithCounterDto<D> {
    private List<D> data;
    private long total;
}
