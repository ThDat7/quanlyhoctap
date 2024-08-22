package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(nullable = false, unique = true)
    private String code;
    private Float credits;

    private Integer sessionInWeek;
    private Integer theoryPeriod;
    private Integer practicePeriod;

    @OneToMany(mappedBy = "course")
    private Set<CourseOutline> courseOutlines;

    @OneToMany(mappedBy = "course")
    private Set<EducationProgramCourse> educationProgramCourses;

    @OneToMany(mappedBy = "course")
    private Set<CourseClass> courseClasses;
}
