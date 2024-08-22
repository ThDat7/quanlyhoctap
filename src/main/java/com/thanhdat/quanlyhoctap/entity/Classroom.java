package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Boolean isAvailable;
    private RoomType roomType;

    @OneToMany(mappedBy = "classroom")
    private Set<ScheduleStudy> scheduleStudies;
}
