package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.TransferDTO;
import com.digitalhouse.money.accountservice.exceptionhandler.InsufficientFundsException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Tag(name="Transfers")
@SecurityRequirement(name = "BearerAuth")
public class TransferController {

    private final TransactionService service;

    public TransferController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/accounts/{accountId}/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferDTO sendTransfer(@PathVariable UUID accountId, @RequestBody TransferDTO request) throws
            UnauthorizedException, ResourceNotFoundException, InsufficientFundsException {
        return service.save(request,accountId);
    }
}
