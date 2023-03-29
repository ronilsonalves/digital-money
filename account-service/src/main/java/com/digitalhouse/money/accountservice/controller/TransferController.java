package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.*;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.InsufficientFundsException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.response.PageResponse;
import com.digitalhouse.money.accountservice.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Transfers")
@SecurityRequirement(name = "BearerAuth")
public class TransferController {

    private final TransactionService service;

    public TransferController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/accounts/{accountId}/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDTO sendTransfer(@PathVariable UUID accountId, @RequestBody TransferRequestDTO request) throws
            UnauthorizedException, ResourceNotFoundException, InsufficientFundsException {
        return service.save(request, accountId);
    }

    @GetMapping("/accounts/{accountId}/transfers")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ITransferResponseDTO> listTransfers(
            @PathVariable() UUID accountId,
            @RequestParam(defaultValue = "TRANSFERÊNCIA") TransactionType transactionType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) throws UnauthorizedException, ResourceNotFoundException {
        List<ITransferResponseDTO> transfers;
        Pageable paging = PageRequest.of(page - 1, pageSize);
        Page<ITransferResponseDTO> pageTransfers = service.listAllByAccountOriginAndTypeOrdByDate(accountId, transactionType,
                paging);

        transfers = pageTransfers.getContent();

        return PageResponse.<ITransferResponseDTO>builder()
                .data(transfers)
                .currentPage(pageTransfers.getNumber() + 1)
                .totalItems(pageTransfers.getTotalElements())
                .totalPages(pageTransfers.getTotalPages())
                .build();

    }

    @GetMapping("/accounts/{accountId}/transfers/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<LastFiveTransfersDTO> listLastFiveRecentAccounts(@PathVariable UUID accountId) throws UnauthorizedException, ResourceNotFoundException {
        return service.listLastFiveAccountsTransferred(accountId);
    }
}
