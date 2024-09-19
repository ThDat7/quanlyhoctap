package com.thanhdat.quanlyhoctap.entity;
import jakarta.persistence.*;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "education_program_courses",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"course_id", "education_program_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgramCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    Semester semester;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "education_program_id", nullable = false)
    EducationProgram educationProgram;

    @ManyToOne
    @JoinColumn(name = "course_outline_id")
    CourseOutline courseOutline;
}
