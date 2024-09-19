package com.thanhdat.quanlyhoctap.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String teacherCode;
    @OneToOne(cascade = CascadeType.ALL)
    User user;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    Faculty faculty;

    @OneToMany(mappedBy = "teacher")
    Set<CourseOutline> courseOutlines;

    @OneToMany(mappedBy = "teacher")
    Set<CourseClass> courseClass;

    @Transient
    public String getFullName() {
        return user.getLastName() + " " + user.getFirstName();
    }
}
