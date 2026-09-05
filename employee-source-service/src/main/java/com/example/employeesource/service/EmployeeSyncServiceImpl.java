package com.example.employeesource.service;

import com.example.employeesource.dto.EmployeeDto;
import com.example.employeesource.entity.LegacyEmployee;
import com.example.employeesource.entity.SourceEmployee;
import com.example.employeesource.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmployeeSyncServiceImpl {

	private final LegacyEmployeeServiceImpl legacyEmployeeServiceImpl;
	private final SourceEmployeeServiceImpl sourceEmployeeServiceImpl;
	private final RestTemplate restTemplate;

	@Value("${employee.receiver.url}")
	private String receiverUrl;

	public EmployeeSyncServiceImpl(LegacyEmployeeServiceImpl legacyEmployeeServiceImpl, SourceEmployeeServiceImpl sourceEmployeeServiceImpl,
			RestTemplate restTemplate) {
		this.legacyEmployeeServiceImpl = legacyEmployeeServiceImpl;
		this.sourceEmployeeServiceImpl = sourceEmployeeServiceImpl;
		this.restTemplate = restTemplate;
	}

	public List<SourceEmployee> sendAllEmployees() throws Exception {

		List<LegacyEmployee> legacyEmployees = legacyEmployeeServiceImpl.getFiveEmployees();

		List<EmployeeDto> employeeDtos = legacyEmployees.stream().map(this::toDto).toList();

		try {
			sendToReceiver(employeeDtos);
		} catch (RuntimeException ex) {
			// sending failed after commit; data remains persisted in source_employee
			throw new ApiException(
					"Failed to push employees to receiver service after saving. Cause: " + ex.getMessage());
		}

		legacyEmployeeServiceImpl.updateAllWaitingEmployee(legacyEmployees);

		// saveAll uses the repository's transaction and will commit when it returns
		List<SourceEmployee> savedEmployees = sourceEmployeeServiceImpl.saveAll(employeeDtos);

		return savedEmployees;
	}

	public void sendToReceiver(List<EmployeeDto> employeeDtos) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<EmployeeDto>> request = new HttpEntity<>(employeeDtos, headers);
		ResponseEntity<String> response = restTemplate.exchange(receiverUrl, HttpMethod.POST, request, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new ApiException("Failed to push employees to receiver service. Status: " + response.getStatusCode());
		}
	}

	private EmployeeDto toDto(LegacyEmployee employee) {
		return EmployeeDto.builder().id(employee.getId()).name(employee.getName()).email(employee.getEmail())
				.department(employee.getDepartment()).salary(employee.getSalary())
				.phoneNumber(employee.getPhoneNumber()).createdAt(employee.getCreatedAt()).build();
	}
}
