package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "schedule_studies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer shiftStart;
    Integer shiftLength;

    LocalDate startDate;
    Integer weekLength;

    @ManyToOne
    @JoinColumn(name = "course_class_id")
    CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    Classroom classroom;
}
