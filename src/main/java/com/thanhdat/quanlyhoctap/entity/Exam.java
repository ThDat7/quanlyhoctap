package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDateTime startTime;
    LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "course_class_id")
    CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    Classroom classroom;

    ExamType type;
}
