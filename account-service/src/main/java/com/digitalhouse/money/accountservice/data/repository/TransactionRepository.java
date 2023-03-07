package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByOriginAccountNumber(UUID accountId);
}