package com.example.employeesource.service;

import org.springframework.stereotype.Service;

import com.example.employeesource.entity.EmployeeData;
import com.example.employeesource.entity.LegacyEmployee;
import com.example.employeesource.exception.ApiException;
import com.example.employeesource.repository.LegacyEmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeDataServiceImpl {
	
  private final LegacyEmployeeRepository legacyEmployeeRepository;
  
  
public LegacyEmployee saveData(EmployeeData employeeData) {
	  
	LegacyEmployee byEmail = legacyEmployeeRepository.getByEmail(employeeData.getEmail());
	  if (byEmail!=null) {
		  throw new ApiException("Employee Data Already Exist with this Email");
		
	}
	  
	  LegacyEmployee legacyEmployee=new LegacyEmployee();
	  
	  if(byEmail==null) {
		  legacyEmployee.setName(employeeData.getName());
		  legacyEmployee.setDepartment(employeeData.getDepartment());
		  legacyEmployee.setCreatedAt(employeeData.getCreatedAt());
		  legacyEmployee.setEmail(employeeData.getEmail());
		  legacyEmployee.setFlag(employeeData.getFlag());
		  legacyEmployee.setPhoneNumber(employeeData.getPhoneNumber());
		  legacyEmployee.setSalary(employeeData.getSalary());
		  
	  }
	  
	 
	  return legacyEmployeeRepository.save(legacyEmployee);
	
}
	
	

}
