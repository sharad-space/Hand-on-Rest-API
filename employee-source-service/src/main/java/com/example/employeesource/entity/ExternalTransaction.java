package com.example.employeesource.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "external_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalTransactionId;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private String customerCode;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String requestId;

    @Column(nullable = false)
    private String sourceSystem;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String schemaVersion;

    @Column(nullable = false)
    private LocalDateTime requestTimestamp;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalTransactionItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalTransactionPayment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalTransactionAddress> addresses = new ArrayList<>();
}
