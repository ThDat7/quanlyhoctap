package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsViewResponse {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
