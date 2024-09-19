package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "faculties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String alias;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    Set<Major> majors;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    Set<Teacher> teachers;
}
