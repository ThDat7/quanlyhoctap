package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "final_exams")
@Data
@NoArgsConstructor
public class FinalExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "course_class_id")
    private CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
