package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "schedule_studies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer dayInWeek;
    private Integer shiftStart;
    private Integer shiftLength;

    private Date startDate;
    private Integer weekLength;

    @ManyToOne
    @JoinColumn(name = "course_class_id")
    private CourseClass courseClass;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
