package com.example.employeesource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeesource.entity.RequestAuditLog;

@Repository
public interface RequestAuditLogRepository extends JpaRepository<RequestAuditLog, Long> {
}
