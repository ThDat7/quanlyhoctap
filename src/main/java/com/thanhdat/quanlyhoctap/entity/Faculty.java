package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "faculties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String alias;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Set<Major> majors;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Set<Teacher> teachers;
}
