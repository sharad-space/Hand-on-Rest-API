package com.example.employeesource.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalTransactionRequestDto {

    @NotBlank(message = "requestId is required")
    @Size(min = 3, max = 100, message = "requestId length must be between 3 and 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "requestId contains invalid characters")
    private String requestId;

    @NotBlank(message = "sourceSystem is required")
    @Size(min = 2, max = 100, message = "sourceSystem length must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9_\\- ]+$", message = "sourceSystem contains invalid characters")
    private String sourceSystem;

    @NotBlank(message = "eventType is required")
    @Size(min = 2, max = 100, message = "eventType length must be between 2 and 100 characters")
    @Pattern(regexp = "^[A-Za-z0-9_\\- ]+$", message = "eventType contains invalid characters")
    private String eventType;

    @NotBlank(message = "schemaVersion is required")
    @Size(min = 1, max = 20, message = "schemaVersion length must be between 1 and 20 characters")
    @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "schemaVersion contains invalid characters")
    private String schemaVersion;

    @NotNull(message = "requestTimestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestTimestamp;

    @NotEmpty(message = "transactions cannot be empty")
    @Valid
    private List<ExternalTransactionDto> transactions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExternalTransactionDto {

        @NotBlank(message = "externalTransactionId is required")
        @Size(min = 3, max = 100, message = "externalTransactionId length must be between 3 and 100 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "externalTransactionId contains invalid characters")
        private String externalTransactionId;

        @NotNull(message = "transactionDate is required")
        private LocalDate transactionDate;

        @NotBlank(message = "customerCode is required")
        @Size(min = 2, max = 100, message = "customerCode length must be between 2 and 100 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "customerCode contains invalid characters")
        private String customerCode;

        @NotBlank(message = "currency is required")
        @Size(min = 3, max = 10, message = "currency length must be between 3 and 10 characters")
        @Pattern(regexp = "^[A-Z]{3,10}$", message = "currency must be in ISO format like INR, USD")
        private String currency;

        @NotNull(message = "totalAmount is required")
        @DecimalMin(value = "0.01", message = "totalAmount must be greater than 0")
        private BigDecimal totalAmount;

        @NotBlank(message = "status is required")
        @Pattern(regexp = "^[A-Z_]+$", message = "status contains invalid characters")
        private String status;

        @Valid
        @NotEmpty(message = "items cannot be empty")
        private List<ItemDto> items;

        @Valid
        @NotEmpty(message = "payments cannot be empty")
        private List<PaymentDto> payments;

        @Valid
        @NotEmpty(message = "addresses cannot be empty")
        private List<AddressDto> addresses;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {
        @NotBlank(message = "itemCode is required")
        @Size(min = 3, max = 100, message = "itemCode length must be between 3 and 100 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "itemCode contains invalid characters")
        private String itemCode;

        @NotBlank(message = "description is required")
        @Size(min = 2, max = 255, message = "description length must be between 2 and 255 characters")
        @Pattern(regexp = "^[A-Za-z0-9 _.,:/()\\-]+$", message = "description contains invalid characters")
        private String description;

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity must be greater than 0")
        @Positive(message = "quantity must be a positive integer")
        private Integer quantity;

        @NotNull(message = "unitPrice is required")
        @DecimalMin(value = "0.01", message = "unitPrice must be greater than 0")
        @Positive(message = "unitPrice must be a positive number")
        private BigDecimal unitPrice;

        @NotNull(message = "totalPrice is required")
        @DecimalMin(value = "0.01", message = "totalPrice must be greater than 0")
        private BigDecimal totalPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDto {
        @NotBlank(message = "paymentMode is required")
        @Pattern(regexp = "^[A-Z_]+$", message = "paymentMode contains invalid characters")
        private String paymentMode;

        @NotBlank(message = "paymentReference is required")
        @Size(min = 3, max = 100, message = "paymentReference length must be between 3 and 100 characters")
        @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "paymentReference contains invalid characters")
        private String paymentReference;

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than 0")
        private BigDecimal amount;

        @NotBlank(message = "paymentStatus is required")
        @Pattern(regexp = "^[A-Z_]+$", message = "paymentStatus contains invalid characters")
        private String paymentStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        @NotBlank(message = "addressType is required")
        @Pattern(regexp = "^[A-Z_]+$", message = "addressType contains invalid characters")
        private String addressType;

        @Pattern(regexp = "^[A-Za-z0-9 .,-]+$", message = "city contains invalid characters")
        private String city;

        @Pattern(regexp = "^[A-Za-z0-9 .,-]+$", message = "state contains invalid characters")
        private String state;

        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "pincode contains invalid characters")
        private String pincode;
    }
}
