package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.AccountCreateRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody AccountCreateRequestDTO dto) throws ConflictException {
        Account account = Account.builder().userId(dto.getUserId()).available_amount(new BigDecimal(0)).build();
        return service.save(account);
    }

    @GetMapping("account/{account_id}")
    public  Account getAccountById(@RequestParam UUID account_id){
        return service.getById(account_id);
    }
}
