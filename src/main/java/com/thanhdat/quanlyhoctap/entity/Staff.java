package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "staffs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @Transient
    public String getFullName() {
        return user.getLastName() + " " + user.getFirstName();
    }
}
