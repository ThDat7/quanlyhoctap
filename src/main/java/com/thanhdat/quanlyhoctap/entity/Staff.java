package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "staffs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String code;
    @OneToOne(cascade = CascadeType.ALL)
    User user;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    Faculty faculty;

    @Transient
    public String getFullName() {
        return user.getLastName() + " " + user.getFirstName();
    }
}
