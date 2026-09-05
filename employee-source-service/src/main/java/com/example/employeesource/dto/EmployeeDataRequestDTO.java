package com.example.employeesource.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.Data;


@Data
public class EmployeeDataRequestDTO {
	

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
    
    @Column(nullable = false)
    private String flag = "W";

}
