package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "course_outlines")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseOutline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_rule_id")
    CourseRule courseRule;

    String url;

    int yearPublished;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    Teacher teacher;

    @Column(name = "status", length = 15, columnDefinition = "varchar(15) default 'DOING'", nullable = false)
    @Enumerated(EnumType.STRING)
    OutlineStatus status;
    LocalDateTime deadline;

    @OneToMany(mappedBy = "courseOutline")
    Set<EducationProgramCourse> educationProgramCourses;
}
