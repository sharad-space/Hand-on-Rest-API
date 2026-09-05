package com.example.employeesource.repository;

import com.example.employeesource.entity.EmployeeData;
import com.example.employeesource.entity.LegacyEmployee;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyEmployeeRepository extends JpaRepository<LegacyEmployee, Long> {

	@Query(value = """
			SELECT *
			FROM employee
			WHERE flag = 'W'
			ORDER BY id
			LIMIT 5
			""", nativeQuery = true)
	List<LegacyEmployee> findTop5WaitingEmployees();

	// Update employee flag to Processed
	@Modifying
	@Transactional
	@Query(value = """
			UPDATE employee
			SET flag = 'P'
			WHERE id = :employeeId
			""", nativeQuery = true)
	void updateEmployeeFlagToProcessed(Long employeeId);
	
	public LegacyEmployee getByEmail(String email);

}
