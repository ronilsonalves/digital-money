package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.AccountCreateRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
@Tag(name = "Accounts")
@SecurityRequirement(name = "BearerAuth")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody AccountCreateRequestDTO dto) throws ConflictException {
        Account account = Account.builder().userId(dto.getUserId()).available_amount(new BigDecimal(0)).build();
        return service.save(account);
    }

    @GetMapping("accounts/{account_id}")
    @PreAuthorize("isAuthenticated()")
    public Account getAccountById(@PathVariable UUID account_id) {
        System.out.println(account_id);
        return service.getById(account_id);
    }

    @GetMapping("accounts")
    @PreAuthorize("isAuthenticated()")
    public Account getMyAccount() throws ResourceNotFoundException {
        return service.getByLoggedUser();
    }
}
