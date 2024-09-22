package com.thanhdat.quanlyhoctap.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseType {
    SPECIALIZE("Chuyên ngành"),
    GENERAL("Đại cương"),
    ;

    private final String description;
}
