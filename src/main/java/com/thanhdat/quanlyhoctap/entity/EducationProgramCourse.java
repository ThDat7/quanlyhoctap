package com.thanhdat.quanlyhoctap.entity;
import jakarta.persistence.*;


import lombok.*;

@Entity
@Table(name = "education_program_courses",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"course_id", "education_program_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationProgramCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int semester;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "education_program_id", nullable = false)
    private EducationProgram educationProgram;

    @ManyToOne
    @JoinColumn(name = "course_outline_id")
    private CourseOutline courseOutline;
}
