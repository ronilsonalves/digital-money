package com.digitalhouse.money.accountservice.data.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Serial
    private static final long serialVersionUID = 1912491329356572322L;

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID Id;

    @Column(precision = 10, scale = 2)
    private BigDecimal available_amount;

    @JsonProperty("user_id")
    @Column(nullable = false, unique = true)
    private UUID userId;
}
