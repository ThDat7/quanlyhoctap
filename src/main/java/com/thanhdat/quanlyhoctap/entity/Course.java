package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "courses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    @Column(nullable = false, unique = true)
    String code;
    Float credits;
    CourseType type;

    Integer sessionInWeek;
    Integer theoryPeriod;
    Integer practicePeriod;

    @OneToMany(mappedBy = "course")
    Set<CourseOutline> courseOutlines;

    @OneToMany(mappedBy = "course")
    Set<EducationProgramCourse> educationProgramCourses;

    @OneToMany(mappedBy = "course")
    Set<CourseClass> courseClasses;
}
