package com.example.employeesource.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeesource.dto.EmployeeDataRequestDTO;
import com.example.employeesource.entity.EmployeeData;
import com.example.employeesource.entity.LegacyEmployee;
import com.example.employeesource.entity.SourceEmployee;
import com.example.employeesource.service.EmployeeDataServiceImpl;
import com.example.employeesource.service.EmployeeSyncServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/source")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeSyncServiceImpl employeeSyncServiceImpl;
	private final EmployeeDataServiceImpl employeeDataServiceImpl;

	@PostMapping("/save-data")
	public ResponseEntity<LegacyEmployee> submitData(@Valid @RequestBody EmployeeData employeeDataRequestDTO) {
		LegacyEmployee saveData = employeeDataServiceImpl.saveData(employeeDataRequestDTO);

		return ResponseEntity.ok(saveData);

	}

	@PostMapping("/send-data")
	public ResponseEntity<List<SourceEmployee>> sendEmployeesData() throws Exception {
		List<SourceEmployee> savedEmployees = employeeSyncServiceImpl.sendAllEmployees();
		return ResponseEntity.ok(savedEmployees);
	}

}
