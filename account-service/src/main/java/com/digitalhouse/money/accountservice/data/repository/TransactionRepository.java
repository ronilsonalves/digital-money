package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.dto.ILastFiveTransfers;
import com.digitalhouse.money.accountservice.data.dto.ITransferResponseDTO;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByOriginAccountNumber(UUID accountId);

    @Query("SELECT t FROM Transaction t WHERE t.originAccountNumber = ?1 and t.transactionType = ?2 ORDER BY t" +
            ".transactionDate DESC")
    Page<ITransferResponseDTO> findAllByOriginAccountNumberAndTransactionTypeOrderByTransactionDate(
            UUID origin, TransactionType type, Pageable pageable
            );

    @Query("SELECT DISTINCT t.recipientAccountNumber AS transactionDestination, MAX(t.transactionDate) AS " +
            "transactionDate FROM Transaction t " +
            "WHERE t.originAccountNumber = ?1 AND t.transactionType = ?2 GROUP BY transactionDestination")
    List<ILastFiveTransfers> findTop5RecipientAccountNumberAndTransactionDateByOriginAccountNumber(
            UUID originAccountNumber,
            TransactionType transactionType);
}