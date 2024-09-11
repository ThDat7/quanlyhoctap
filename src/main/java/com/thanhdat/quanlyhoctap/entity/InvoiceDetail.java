package com.thanhdat.quanlyhoctap.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "course_class_id")
    private CourseClass courseClass;
    private Integer tuition;
}
