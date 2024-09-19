package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "student_classes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer year;
    String classOrder;

    @OneToMany(mappedBy = "studentClass", cascade = CascadeType.ALL)
    Set<Student> students;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    Major major;

    @OneToMany(mappedBy = "studentClass")
    Set<CourseClass> courseClasses;

    @Transient
    public String getName() {
//        DH string will get from config file
        return "DH" + (year - 2000) + major.getAlias() + classOrder;
    }
}
