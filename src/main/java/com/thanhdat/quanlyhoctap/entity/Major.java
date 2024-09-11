package com.thanhdat.quanlyhoctap.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "majors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String alias;

    private Integer specializeTuition;
    private Integer generalTuition;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "major")
    private Set<EducationProgram> educationPrograms;

    @OneToMany(mappedBy = "major")
    private Set<StudentClass> studentClasses;
}