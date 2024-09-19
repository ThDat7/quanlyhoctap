package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String studentCode;
    @OneToOne(cascade = CascadeType.ALL)
    User user;
    @ManyToOne
    @JoinColumn(name = "student_class_id", nullable = false)
    StudentClass studentClass;

    @OneToMany(mappedBy = "student")
    Set<Study> studies;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    Set<StudentStatus> studentStatuses;
}
