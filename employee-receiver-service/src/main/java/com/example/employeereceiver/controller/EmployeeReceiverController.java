package com.example.employeereceiver.controller;

import com.example.employeereceiver.dto.EmployeeDto;
import com.example.employeereceiver.entity.ReceiverEmployee;
import com.example.employeereceiver.service.ReceiverEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receiver")
@RequiredArgsConstructor
public class EmployeeReceiverController {

    private final ReceiverEmployeeService receiverEmployeeService;

    @PostMapping("/receive")
    public ResponseEntity<String> receiveEmployees(@Valid @RequestBody List<EmployeeDto> employees) {
        receiverEmployeeService.saveEmployees(employees);
        return ResponseEntity.ok("Employee data received and saved successfully");
    }

    @GetMapping("/employees")
    public List<ReceiverEmployee> getSavedEmployees() {
        return receiverEmployeeService.getAllEmployees();
    }
}
