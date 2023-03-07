package com.digitalhouse.money.accountservice.service;

import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    /**
     * Returns a Data Transfer Object for a transaction created successfully
     * @param transactionData a transaction request data
     * @param accountId the account number to register a transaction as originAccount
     * @return a Data Transfer Object for a transaction created successfully
     * @throws ResourceNotFoundException when accountID
     * provided not exists.
     */
    TransactionResponseDTO save(TransactionRequestDTO transactionData, UUID accountId) throws ResourceNotFoundException;

    /**
     * Returns a list of Transactions DTOs by account
     * @param accountId is used to fetch data of transactions.
     * @return a list of transactions
     * @throws ResourceNotFoundException if the account provided not exists.
     */
    List<TransactionResponseDTO> listAllTransactionsByAccount(UUID accountId) throws ResourceNotFoundException;
}
