package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository repository;
    @Override
    public Account save(Account account) {
        if(repository.existsByUserId(account.getUserId()))
            throw  new ConflictException("User already has account");

        return repository.save(account);
    }

    @Override
    public Account getById(UUID account_id) {
        return repository.findById(account_id).orElseThrow(()-> new ResourceNotFoundException("Account not found"));
    }
}
