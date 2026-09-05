package com.example.employeesource.service;

import com.example.employeesource.entity.LegacyEmployee;
import com.example.employeesource.exception.ResourceNotFoundException;
import com.example.employeesource.repository.LegacyEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LegacyEmployeeServiceImpl {

    private final LegacyEmployeeRepository legacyEmployeeRepository;

    public List<LegacyEmployee> getFiveEmployees() {
        List<LegacyEmployee> employees = legacyEmployeeRepository.findTop5WaitingEmployees();
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employee records found in the legacy employee table");
        }
        return employees;
    }
    
    void updateAllWaitingEmployee(List<LegacyEmployee> employees) {
    	
         for (LegacyEmployee legacyEmployee : employees) {
        	 legacyEmployeeRepository.updateEmployeeFlagToProcessed(legacyEmployee.getId());
			
		}
    	
    	
    }
}
