package com.thanhdat.quanlyhoctap.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    private FactorScore factorScore;
    private Double score;
}
