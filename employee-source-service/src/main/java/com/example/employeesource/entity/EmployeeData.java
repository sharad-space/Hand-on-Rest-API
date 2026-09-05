package com.example.employeesource.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
public class EmployeeData {

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

	@Column(nullable = false)
	private String flag = "W";

}
