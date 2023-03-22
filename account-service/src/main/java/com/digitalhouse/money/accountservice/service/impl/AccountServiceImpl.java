package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.AccountService;
import com.digitalhouse.money.accountservice.util.MailConstructor;
import com.digitalhouse.money.accountservice.util.VerifyAuthenticationUtil;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    private final VerifyAuthenticationUtil verifyAuthenticationUtil;

    @Override
    public Account save(Account account) {
        if(repository.existsByUserId(account.getUserId()))
            throw new ConflictException("User already has account");

        return repository.save(account);
    }

    @Override
    public Account getById(UUID account_id) throws ResourceNotFoundException, UnauthorizedException {
        Account account = repository.findById(account_id).orElseThrow(()-> new ResourceNotFoundException("Account not" +
                " found."));
        if (!verifyAuthenticationUtil.isUserUUIDSameFromAuth(account.getUserId()))
            throw new UnauthorizedException("User not authorized to perform this action.");
        return account;
    }
}
