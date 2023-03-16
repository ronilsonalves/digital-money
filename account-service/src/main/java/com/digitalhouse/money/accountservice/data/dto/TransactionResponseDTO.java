package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    private UUID uuid;

    private UUID originAccountNumber;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
