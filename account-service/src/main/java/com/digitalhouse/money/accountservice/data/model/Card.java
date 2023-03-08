package com.digitalhouse.money.accountservice.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Cards")
public class Card {

    @Serial
    private static final long serialVersionUID = 1912491329356572322L;

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @JsonProperty("account_id")
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private Long number;

    @Column(name = "expiration_date", nullable = false)
    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonProperty("first_last_name")
    @Column(nullable = false, name = "first_last_name")
    private String firstLastName;

    @Column
    private Integer cvc;

}
