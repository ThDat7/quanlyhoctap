package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    private Date startDate;
    private Integer durationWeeks;

    @OneToMany(mappedBy = "semester")
    private Set<CourseClass> courseClasses;
}
