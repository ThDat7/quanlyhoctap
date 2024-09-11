package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;

    private String title;
    private String content;
    private Boolean isImportant;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Staff author;
}
