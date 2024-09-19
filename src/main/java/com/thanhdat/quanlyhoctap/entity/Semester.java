package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "semesters",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"year", "semester"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer year;
    Integer semester;
    LocalDate startDate;
    Integer durationWeeks;
    LocalDateTime lockTime;

    @OneToMany(mappedBy = "semester")
    Set<CourseClass> courseClasses;
}
