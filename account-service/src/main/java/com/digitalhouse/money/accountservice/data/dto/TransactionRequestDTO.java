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
public class TransactionRequestDTO {

    private UUID originAccountNumber;
    private UUID cardIdentification;
    private UUID recipientAccountNumber;
    @NotNull
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    @NotNull
    private TransactionType transactionType;
    private String description;
}
