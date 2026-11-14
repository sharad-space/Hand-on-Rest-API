package com.example.employeesource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "external_transaction_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransactionAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressType;

    private String city;
    private String state;
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private ExternalTransaction transaction;
}
