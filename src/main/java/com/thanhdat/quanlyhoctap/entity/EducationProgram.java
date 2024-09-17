package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "education_programs",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"major_id", "schoolYear"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int schoolYear;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @OneToMany(mappedBy = "educationProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationProgramCourse> educationProgramCourses;
}
