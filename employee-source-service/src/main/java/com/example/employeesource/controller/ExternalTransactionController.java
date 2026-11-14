package com.example.employeesource.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeesource.dto.ExternalTransactionRequestDto;
import com.example.employeesource.dto.TransactionProcessingSummaryDto;
import com.example.employeesource.dto.TransactionSaveResponseDto;
import com.example.employeesource.entity.ExternalTransaction;
import com.example.employeesource.exception.ApiException;
import com.example.employeesource.service.ExternalTransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExternalTransactionController {

    private final ExternalTransactionService externalTransactionService;

    @PostMapping("/transaction")
    public ResponseEntity<TransactionProcessingSummaryDto> saveTransactions(@Valid @RequestBody ExternalTransactionRequestDto requestDto) {
        try {
            validateRequestBeforeService(requestDto);
            TransactionProcessingSummaryDto response = externalTransactionService.processTransactions(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            externalTransactionService.logFailureAudit(
                    requestDto != null ? requestDto.getRequestId() : "UNKNOWN",
                    requestDto != null ? requestDto.getSourceSystem() : null,
                    requestDto != null ? requestDto.getEventType() : null,
                    requestDto,
                    ex.getMessage()
            );
            throw ex;
        }
    }

    private void validateRequestBeforeService(ExternalTransactionRequestDto requestDto) {
        if (requestDto == null) {
            throw new ApiException("Request body is required");
        }

        if (requestDto.getTransactions() == null || requestDto.getTransactions().isEmpty()) {
            throw new ApiException("Transactions list cannot be empty");
        }

        if (requestDto.getTransactions().size() > 100) {
            throw new ApiException("Too many transactions in one request. Maximum allowed is 100");
        }

        Set<String> seenTransactionIds = new HashSet<>();
        for (ExternalTransactionRequestDto.ExternalTransactionDto transaction : requestDto.getTransactions()) {
            if (transaction == null || transaction.getExternalTransactionId() == null || transaction.getExternalTransactionId().isBlank()) {
                throw new ApiException("externalTransactionId is required for each transaction");
            }

            String transactionId = transaction.getExternalTransactionId().trim();
            if (!transactionId.matches("^[A-Za-z0-9_-]+$")) {
                throw new ApiException("Invalid externalTransactionId: " + transactionId + ". Only letters, numbers, underscores and hyphens are allowed.");
            }

            if (!seenTransactionIds.add(transactionId)) {
                throw new ApiException("Duplicate externalTransactionId found in request: " + transactionId);
            }
        }
    }
}
