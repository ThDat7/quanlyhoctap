package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "course_classes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "course_rule_id")
    private CourseRule courseRule;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;

    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Study> studies;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ScheduleStudy> scheduleStudies;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exam> exams;
}
