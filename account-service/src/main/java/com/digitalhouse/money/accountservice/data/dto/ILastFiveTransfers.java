package com.digitalhouse.money.accountservice.data.dto;


import java.time.LocalDate;
import java.util.UUID;

public interface ILastFiveTransfers {
    UUID getTransactionDestination();
    LocalDate getTransactionDate();
}
