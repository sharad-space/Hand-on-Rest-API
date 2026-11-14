package com.example.employeesource.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionProcessingSummaryDto {

    private boolean success;
    private String message;
    private String requestId;
    private int totalRequested;
    private int totalSaved;
    private int totalFailed;
//    private List<Long> savedTransactionIds;
    private List<String> failedTransactionIds;
    private List<String> failedReasons;
}
