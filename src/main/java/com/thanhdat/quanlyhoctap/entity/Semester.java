package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "semesters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer year;
    private Integer semester;
    private LocalDate startDate;
    private Integer durationWeeks;
    private LocalDateTime lockTime;

    @OneToMany(mappedBy = "semester")
    private Set<CourseClass> courseClasses;
}
