package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
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
    private Date deadlineDate;

    @OneToMany(mappedBy = "courseOutline")
    private Set<EducationProgramCourse> educationProgramCourses;
}
