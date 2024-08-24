package com.thanhdat.quanlyhoctap.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String teacherCode;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "teacher")
    private Set<CourseOutline> courseOutlines;

    @OneToMany(mappedBy = "teacher")
    private Set<CourseClass> courseClass;

    @Transient
    public String getFullName() {
        return user.getLastName() + " " + user.getFirstName();
    }
}
