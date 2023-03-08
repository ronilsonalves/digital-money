package com.digitalhouse.money.accountservice.data.model;

import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    @Serial
    private static final long serialVersionUID = 1912491329356572322L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true,nullable = false)
    private UUID uuid;

    private UUID originAccountNumber; // must be defined as same recipient if card is used in this transaction.

    private UUID cardIdentification;

    private UUID recipientAccountNumber;

    private BigDecimal transactionAmount;

    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    private String description;

}
