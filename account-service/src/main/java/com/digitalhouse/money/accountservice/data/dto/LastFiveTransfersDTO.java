package com.digitalhouse.money.accountservice.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class LastFiveTransfersDTO {
    String accountOwner;
    UUID recipientAccountNumber;
    LocalDate transactionDate;
}
