package com.example.employeesource.repository;

import com.example.employeesource.entity.SourceEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlEmployeeRepository extends JpaRepository<SourceEmployee, Long> {
	
	SourceEmployee getSourceEmpoyeeByEmail(String email);
	
	
}
