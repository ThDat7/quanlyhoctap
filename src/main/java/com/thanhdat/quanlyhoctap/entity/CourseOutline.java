package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "course_outlines")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseOutline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_rule_id")
    private CourseRule courseRule;

    private String url;

    private int yearPublished;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(name = "status", length = 15, columnDefinition = "varchar(15) default 'DOING'", nullable = false)
    @Enumerated(EnumType.STRING)
    private OutlineStatus status;
    private LocalDateTime deadline;

    @OneToMany(mappedBy = "courseOutline")
    private Set<EducationProgramCourse> educationProgramCourses;
}
