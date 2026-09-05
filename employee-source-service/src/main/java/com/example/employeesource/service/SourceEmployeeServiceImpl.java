package com.example.employeesource.service;

import com.example.employeesource.dto.EmployeeDto;
import com.example.employeesource.entity.SourceEmployee;
import com.example.employeesource.exception.ResourceNotFoundException;
import com.example.employeesource.repository.ControlEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SourceEmployeeServiceImpl {

    private final ControlEmployeeRepository controlEmployeeRepository;

//    public List<SourceEmployee> getAllEmployees() {
//        List<SourceEmployee> employees = controlEmployeeRepository.findAll();
//        if (employees.isEmpty()) {
//            throw new ResourceNotFoundException("No employee records found in the source employee table");
//        }
//        return employees;
//    }

    public List<SourceEmployee> saveAll(List<EmployeeDto> employeeDtos) throws Exception {
    	
    	for (EmployeeDto employeeDto : employeeDtos) {
    		SourceEmployee sourceEmpoyeeByEmail = controlEmployeeRepository.getSourceEmpoyeeByEmail(employeeDto.getEmail());
    		if(sourceEmpoyeeByEmail!=null) {
    			throw new Exception("Sourcce Employee already Exist");
    		}
    			
		}
    	
        return controlEmployeeRepository.saveAll(employeeDtos.stream().map(this::toEntity).toList());
    }

    private SourceEmployee toEntity(EmployeeDto dto) {
        SourceEmployee entity = new SourceEmployee();
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
