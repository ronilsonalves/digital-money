package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseDTO {
    private UUID uuid;
    private UUID originAccountNumber;
    private UUID recipientAccountNumber;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private String description;
}
