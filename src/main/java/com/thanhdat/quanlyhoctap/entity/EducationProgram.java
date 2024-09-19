package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    int schoolYear;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    Major major;

    @OneToMany(mappedBy = "educationProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EducationProgramCourse> educationProgramCourses;
}
