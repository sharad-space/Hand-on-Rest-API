package com.example.employeesource.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeesource.entity.SourceEmployee;
import com.example.employeesource.service.EmployeeSyncServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/source")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeSyncServiceImpl employeeSyncServiceImpl;

    @PostMapping("/send-data")
    public ResponseEntity<List<SourceEmployee>> sendEmployeesData() throws Exception {
        List<SourceEmployee> savedEmployees = employeeSyncServiceImpl.sendAllEmployees();
        return ResponseEntity.ok(savedEmployees);
    }


}
