package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studentCode;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne
    @JoinColumn(name = "student_class_id", nullable = false)
    private StudentClass studentClass;

    @OneToMany(mappedBy = "student")
    private Set<Study> studies;
}
