package com.example.employeesource.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employeesource.entity.ExternalTransaction;

@Repository
public interface ExternalTransactionRepository extends JpaRepository<ExternalTransaction, Long> {

    Optional<ExternalTransaction> findByExternalTransactionId(String externalTransactionId);
}
