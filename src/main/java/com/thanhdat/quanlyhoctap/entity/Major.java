package com.thanhdat.quanlyhoctap.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "majors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String alias;

    Integer specializeTuition;
    Integer generalTuition;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    Faculty faculty;

    @OneToMany(mappedBy = "major")
    Set<EducationProgram> educationPrograms;

    @OneToMany(mappedBy = "major")
    Set<StudentClass> studentClasses;
}