package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_rules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float midTermFactor;
    private Float finalTermFactor;
    private Float passScore;
}
