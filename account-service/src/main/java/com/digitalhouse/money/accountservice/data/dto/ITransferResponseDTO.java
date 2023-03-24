package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public interface ITransferResponseDTO {
    UUID getUuid();
    UUID getOriginAccountNumber();
    UUID getRecipientAccountNumber();
    BigDecimal getTransactionAmount();
    LocalDate getTransactionDate();
    TransactionType getTransactionType();
    String getDescription();
}
