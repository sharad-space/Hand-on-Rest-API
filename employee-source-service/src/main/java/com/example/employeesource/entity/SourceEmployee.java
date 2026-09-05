package com.example.employeesource.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "control_employee_tb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
