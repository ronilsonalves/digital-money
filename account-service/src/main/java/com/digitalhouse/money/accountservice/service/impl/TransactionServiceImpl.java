package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.dto.TransferDTO;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.data.repository.CardRepository;
import com.digitalhouse.money.accountservice.data.repository.TransactionRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.InsufficientFundsException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.TransactionService;
import com.digitalhouse.money.accountservice.util.VerifyAuthenticationUtil;
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

    private final VerifyAuthenticationUtil verifyAuthenticationUtil;

    /**
     * Returns a Data Transfer Object for a transaction created successfully
     *
     * @param transactionData a transaction request data
     * @param accountId       the account number to register a transaction as originAccount
     * @return a Data Transfer Object for a transaction created successfully
     * @throws ResourceNotFoundException when accountID
     *                                   provided not exists.
     * @throws UnauthorizedException when user try to access data impersonating another user account
     */
    @Override
    public TransactionResponseDTO save(TransactionRequestDTO transactionData, UUID accountId) throws ResourceNotFoundException {
        if (!accountRepository.existsById(accountId))
            throw new ResourceNotFoundException("There's no account with number provided.");

        if (!verifyAuthenticationUtil.isUserUUIDSameFromAuth(accountId))
            throw new UnauthorizedException("User not authorized");

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
     * Returns a Data Transfer Object for a transfer made successfully
     * @param transferData a transfer request data
     * @param accountId the account number to register a transaction as originAccount
     * @return a Data Transfer Object for a transfer made successfully
     * @throws ResourceNotFoundException when origin and/or recipient account
     * provided not exists.
     * @throws UnauthorizedException when user try to access data impersonating another user account
     */
    @Override
    public TransferDTO save(TransferDTO transferData, UUID accountId) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("There" +
                "'s no account registered with number provided."));
        Account accountRecipient =
                accountRepository.findById(transferData.getRecipientAccountNumber()).orElseThrow(()->
                        new ResourceNotFoundException("Invalid account destination. Please verify the destination " +
                                "number and try again"));

        if(!verifyAuthenticationUtil.isUserUUIDSameFromAuth(accountId))
            throw new UnauthorizedException("User not authorized");

        if(transferData.getTransactionType().equals(TransactionType.TRANSFERÊNCIA)) {
            Transaction toSave = new Transaction();
            TransferDTO response = new TransferDTO();
            if (transferData.getTransactionAmount().compareTo(account.getAvailable_amount()) >= 0)
                throw new InsufficientFundsException("Unable to complete transfer. Account with insufficient funds to" +
                        " complete the transaction");
            BeanUtils.copyProperties(transferData,toSave);
            BeanUtils.copyProperties(repository.save(toSave),response);
            //setting new values to available amount to accounts in transaction
            account.setAvailable_amount(account.getAvailable_amount().subtract(response.getTransactionAmount()));
            accountRecipient.setAvailable_amount(accountRecipient.getAvailable_amount().add(response.getTransactionAmount()));
            accountRepository.save(account);
            accountRepository.save(accountRecipient);
            return response;
        }
        throw new BadRequestException("Invalid fields, please verify the transfer data and try again.");
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
