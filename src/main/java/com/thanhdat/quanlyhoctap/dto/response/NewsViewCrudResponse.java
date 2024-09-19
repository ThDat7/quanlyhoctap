package com.thanhdat.quanlyhoctap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsViewCrudResponse {
    Integer id;
    String title;
    Integer authorId;
    String content;
    Boolean isImportant;
    LocalDateTime createdAt;
}
