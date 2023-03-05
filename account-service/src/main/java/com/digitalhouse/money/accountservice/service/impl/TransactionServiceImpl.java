package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.data.repository.CardRepository;
import com.digitalhouse.money.accountservice.data.repository.TransactionRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    /**
     * Returns a Data Transfer Object for a transaction created successfully
     *
     * @param transactionData a transaction request data
     * @param accountId       the account number to register a transaction as originAccount
     * @return a Data Transfer Object for a transaction created successfully
     * @throws ResourceNotFoundException when accountID
     *                                   provided not exists.
     */
    @Override
    public TransactionResponseDTO save(TransactionRequestDTO transactionData, UUID accountId) throws ResourceNotFoundException {
        if (!accountRepository.existsById(accountId))
            throw new ResourceNotFoundException("There's no account with number provided.");

        if(transactionData.getTransactionType().equals(TransactionType.DEPÓSITO) && cardRepository.existsById(transactionData.getCardIdentification())) {
            Transaction toSave = new Transaction();
            TransactionResponseDTO response = new TransactionResponseDTO();
            Optional<Account> account = accountRepository.findById(accountId);
            transactionData.setOriginAccountNumber(accountId);
            transactionData.setRecipientAccountNumber(accountId);
            transactionData.setTransactionDate(LocalDate.now());
            BeanUtils.copyProperties(transactionData,toSave);
            BeanUtils.copyProperties(repository.save(toSave),response);
            response.setCardEnding(
                    cardRepository.findById(transactionData.getCardIdentification())
                            .get().getNumber().toString().substring(12)
            );
            //setting a new value to available amount
            account.get().setAvailable_amount(account.get().getAvailable_amount().add(response.getTransactionAmount()));

            accountRepository.save(account.get());
            return response;
        }
        throw new BadRequestException("Please verify card used. We're working to expand the ways to you move your " +
                "money in our wallet, soon new methods will be implemented.");
    }

    /**
     * Returns a list of Transactions DTOs by account
     *
     * @param accountId is used to fetch data of transactions.
     * @return a list of transactions
     * @throws ResourceNotFoundException if the account provided not exists.
     */
    @Override
    public List<TransactionResponseDTO> listAllTransactionsByAccount(UUID accountId) throws ResourceNotFoundException {
        return repository.findAllByOriginAccountNumber(accountId).stream().map(this::createResponse).collect(Collectors.toList());
    }

    /**
     * Parses a transaction into a response object
     * @param transaction is a transaction object to be mapped into a DTO
     * @return TransactionResponseDTO.
     */
    private TransactionResponseDTO createResponse(Transaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        BeanUtils.copyProperties(transaction,responseDTO);
        if (transaction.getTransactionType().equals(TransactionType.DEPÓSITO))
            responseDTO.setCardEnding(
                    cardRepository.findById(transaction.getCardIdentification())
                            .get().getNumber().toString().substring(12)
            );
        return responseDTO;
    }
}
