package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TransactionResponseDTO {

    private UUID originAccountNumber;
    @Size(max=4)
    private String cardEnding;
    private UUID recipientAccountNumber;
    @NotNull
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    @NotNull
    private TransactionType transactionType;
    private String description;
}
