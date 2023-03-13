package com.digitalhouse.money.accountservice.service;

import com.digitalhouse.money.accountservice.data.dto.*;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @throws UnauthorizedException when user try to access data impersonating another user account
     */
    TransactionResponseDTO save(TransactionRequestDTO transactionData, UUID accountId) throws ResourceNotFoundException;

    /**
     * Returns a Data Transfer Object for a transfer made successfully
     *
     * @param transferData a transfer request data
     * @param accountId    the account number to register a transaction as originAccount
     * @return a Data Transfer Object for a transfer made successfully
     * @throws ResourceNotFoundException when origin and/or recipient account
     *                                   provided not exists.
     * @throws UnauthorizedException     when user try to access data impersonating another user account
     */
    TransferResponseDTO save(TransferRequestDTO transferData, UUID accountId) throws ResourceNotFoundException;

    /**
     * Returns a list of Transactions DTOs by account
     * @param accountId is used to fetch data of transactions.
     * @return a list of transactions
     * @throws ResourceNotFoundException if the account provided not exists.
     */
    List<TransactionResponseDTO> listAllTransactionsByAccount(UUID accountId) throws ResourceNotFoundException;

    /**
     * Returns a page of objects containing ITransferResponseDTO
     * @param origin is the origin account number
     * @param type of transaction. Default is TransactionType.TRANSFERÊNCIA
     * @param pageable an object to interact with JPA Repository to mount the pageable return
     * @return Returns a page of objects containing ITransferResponseDTO
     * @throws BadRequestException when the type of transactions is different from TransactionType.TRANSFERÊNCIA
     * @throws ResourceNotFoundException when the origin account number is invalid or non-existent.
     * @throws UnauthorizedException when user try to fetch data from another account that's not his.
     */
    Page<ITransferResponseDTO> listAllByAccountOriginAndTypeOrdByDate(UUID origin,
                                                                      TransactionType type,
                                                                      Pageable pageable) throws
            BadRequestException,
            ResourceNotFoundException,
            UnauthorizedException;
}
