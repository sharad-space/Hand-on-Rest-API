package com.example.employeesource.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSaveResponseDto {

    private boolean success;
    private String message;
    private String requestId;
    private List<Long> savedTransactionIds;
    private int totalRecordsSaved;
}
