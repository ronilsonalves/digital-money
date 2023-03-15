package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.model.Transaction;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Tag(name="Transactions")
@SecurityRequirement(name = "BearerAuth")
public class TransactionController {

    private final TransactionService service;


    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO createTransaction(@PathVariable UUID accountId,
                                                    @RequestBody TransactionRequestDTO requestDTO
    ) throws UnauthorizedException, ResourceNotFoundException {
        return service.save(requestDTO,accountId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponseDTO> listAllTransactionsByAccount(@PathVariable UUID accountId)
            throws UnauthorizedException, ResourceNotFoundException {
        return service.listAllTransactionsByAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/activity")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponseDTO> listLastFive(@PathVariable UUID accountId)
            throws UnauthorizedException, ResourceNotFoundException {
        return service.listLastFiveTransactionsByAccount(accountId);
    }
}
