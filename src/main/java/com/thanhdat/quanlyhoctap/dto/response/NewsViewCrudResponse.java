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
    Long id;
    String title;
    Long authorId;
    String content;
    Boolean isImportant;
    LocalDateTime createdAt;
}
