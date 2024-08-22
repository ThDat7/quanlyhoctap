package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "student_classes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer year;
    private String classOrder;

    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL)
    private Set<Student> students;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @OneToMany(mappedBy = "studentClass")
    private Set<CourseClass> courseClasses;

    @Transient
    public String getName() {
//        DH string will get from config file
        return "DH" + (year - 2000) + major.getAlias() + classOrder;
    }
}
