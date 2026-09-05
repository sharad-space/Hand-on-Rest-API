package com.example.employeereceiver.service;

import com.example.employeereceiver.dto.EmployeeDto;
import com.example.employeereceiver.entity.ReceiverEmployee;
import com.example.employeereceiver.exception.ApiException;
import com.example.employeereceiver.repository.ReceiverEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiverEmployeeService {

    private final ReceiverEmployeeRepository receiverEmployeeRepository;

    public List<ReceiverEmployee> saveEmployees(List<EmployeeDto> employeeDtos) {
        if (employeeDtos == null || employeeDtos.isEmpty()) {
            throw new ApiException("Employee payload cannot be empty");
        }

        List<ReceiverEmployee> employees = employeeDtos.stream().map(this::toEntity).toList();
        return receiverEmployeeRepository.saveAll(employees);
    }

    public List<ReceiverEmployee> getAllEmployees() {
        return receiverEmployeeRepository.findAll();
    }

    private ReceiverEmployee toEntity(EmployeeDto dto) {
        ReceiverEmployee entity = new ReceiverEmployee();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(dto.getDepartment());
        entity.setSalary(dto.getSalary());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
