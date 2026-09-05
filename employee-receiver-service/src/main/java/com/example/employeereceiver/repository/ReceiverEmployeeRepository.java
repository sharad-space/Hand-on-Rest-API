package com.example.employeereceiver.repository;

import com.example.employeereceiver.entity.ReceiverEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverEmployeeRepository extends JpaRepository<ReceiverEmployee, Long> {
}
