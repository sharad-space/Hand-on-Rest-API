package com.example.employeesource.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeesource.dto.ExternalTransactionRequestDto;
import com.example.employeesource.dto.TransactionProcessingSummaryDto;
import com.example.employeesource.entity.ExternalTransaction;
import com.example.employeesource.entity.ExternalTransactionAddress;
import com.example.employeesource.entity.ExternalTransactionItem;
import com.example.employeesource.entity.ExternalTransactionPayment;
import com.example.employeesource.entity.RequestAuditLog;
import com.example.employeesource.exception.ApiException;
import com.example.employeesource.repository.ExternalTransactionRepository;
import com.example.employeesource.repository.RequestAuditLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalTransactionService {

    private final ExternalTransactionRepository externalTransactionRepository;
    private final RequestAuditLogRepository requestAuditLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public TransactionProcessingSummaryDto processTransactions(ExternalTransactionRequestDto requestDto) {
        if (requestDto == null || requestDto.getTransactions() == null || requestDto.getTransactions().isEmpty()) {
            throw new ApiException("No transactions provided");
        }

        List<Long> savedTransactionIds = new ArrayList<>();
        List<String> failedTransactionIds = new ArrayList<>();
        List<String> failedReasons = new ArrayList<>();

        for (ExternalTransactionRequestDto.ExternalTransactionDto dto : requestDto.getTransactions()) {
            try {
                ExternalTransaction savedTransaction = saveSingleTransaction(requestDto, dto);
                savedTransactionIds.add(savedTransaction.getId());
            } catch (Exception ex) {
                String failedId = dto != null ? dto.getExternalTransactionId() : "UNKNOWN";
                failedTransactionIds.add(failedId);
                failedReasons.add(ex.getMessage());
                logFailureAudit(requestDto.getRequestId(), requestDto.getSourceSystem(), requestDto.getEventType(), dto, ex.getMessage());
            }
        }

        if (savedTransactionIds.isEmpty()) {
            throw new ApiException("No transactions saved. Failed transaction IDs: " + failedTransactionIds + ", reasons: " + failedReasons);
        }

        logSuccessAudit(requestDto.getRequestId(), requestDto.getSourceSystem(), requestDto.getEventType(), requestDto);

        int totalRequested = requestDto.getTransactions().size();
        int totalSaved = savedTransactionIds.size();
        int totalFailed = failedTransactionIds.size();

        return new TransactionProcessingSummaryDto(
                true,
                "Transactions processed successfully with partial failure tracking",
                requestDto.getRequestId(),
                totalRequested,
                totalSaved,
                totalFailed,
                failedTransactionIds,
                failedReasons
        );
    }

    private ExternalTransaction saveSingleTransaction(ExternalTransactionRequestDto requestDto,
                                                    ExternalTransactionRequestDto.ExternalTransactionDto dto) {
        if (dto == null) {
            throw new ApiException("Transaction payload is null");
        }

        String txId = dto.getExternalTransactionId();
        if (externalTransactionRepository.findByExternalTransactionId(txId).isPresent()) {
            throw new ApiException("Transaction already exists with externalTransactionId: " + txId);
        }

        ExternalTransaction transaction = new ExternalTransaction();
        transaction.setRequestId(requestDto.getRequestId());
        transaction.setSourceSystem(requestDto.getSourceSystem());
        transaction.setEventType(requestDto.getEventType());
        transaction.setSchemaVersion(requestDto.getSchemaVersion());
        transaction.setRequestTimestamp(requestDto.getRequestTimestamp());
        transaction.setExternalTransactionId(txId);
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setCustomerCode(dto.getCustomerCode());
        transaction.setCurrency(dto.getCurrency());
        transaction.setTotalAmount(dto.getTotalAmount());
        transaction.setStatus(dto.getStatus());

        List<ExternalTransactionItem> items = new ArrayList<>();
        for (ExternalTransactionRequestDto.ItemDto itemDto : dto.getItems()) {
            ExternalTransactionItem item = new ExternalTransactionItem();
            item.setItemCode(itemDto.getItemCode());
            item.setDescription(itemDto.getDescription());
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setTotalPrice(itemDto.getTotalPrice());
            item.setTransaction(transaction);
            items.add(item);
        }
        transaction.setItems(items);

        List<ExternalTransactionPayment> payments = new ArrayList<>();
        for (ExternalTransactionRequestDto.PaymentDto paymentDto : dto.getPayments()) {
            ExternalTransactionPayment payment = new ExternalTransactionPayment();
            payment.setPaymentMode(paymentDto.getPaymentMode());
            payment.setPaymentReference(paymentDto.getPaymentReference());
            payment.setAmount(paymentDto.getAmount());
            payment.setPaymentStatus(paymentDto.getPaymentStatus());
            payment.setTransaction(transaction);
            payments.add(payment);
        }
        transaction.setPayments(payments);

        List<ExternalTransactionAddress> addresses = new ArrayList<>();
        for (ExternalTransactionRequestDto.AddressDto addressDto : dto.getAddresses()) {
            ExternalTransactionAddress address = new ExternalTransactionAddress();
            address.setAddressType(addressDto.getAddressType());
            address.setCity(addressDto.getCity());
            address.setState(addressDto.getState());
            address.setPincode(addressDto.getPincode());
            address.setTransaction(transaction);
            addresses.add(address);
        }
        transaction.setAddresses(addresses);

        return externalTransactionRepository.save(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logSuccessAudit(String requestId, String sourceSystem, String eventType, Object payload) {
        saveAudit(requestId, sourceSystem, eventType, "SUCCESS", payload, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailureAudit(String requestId, String sourceSystem, String eventType, Object payload, String errorMessage) {
        saveAudit(requestId, sourceSystem, eventType, "FAILED", payload, errorMessage);
    }

    private void saveAudit(String requestId, String sourceSystem, String eventType, String status, Object payload, String errorMessage) {
        RequestAuditLog auditLog = new RequestAuditLog();
        auditLog.setRequestId(requestId == null ? "UNKNOWN" : requestId);
        auditLog.setSourceSystem(sourceSystem);
        auditLog.setEventType(eventType);
        auditLog.setStatus(status);
        auditLog.setCreatedAt(LocalDateTime.now());

        try {
            auditLog.setPayload(objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            auditLog.setPayload(String.valueOf(payload));
        }

        auditLog.setErrorMessage(errorMessage);
        requestAuditLogRepository.save(auditLog);
    }
}

