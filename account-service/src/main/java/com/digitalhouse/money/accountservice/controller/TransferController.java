package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.*;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.InsufficientFundsException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.response.TransfersPage;
import com.digitalhouse.money.accountservice.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public TransferResponseDTO sendTransfer(@PathVariable UUID accountId, @RequestBody TransferRequestDTO request) throws
            UnauthorizedException, ResourceNotFoundException, InsufficientFundsException {
        return service.save(request,accountId);
    }

    @GetMapping("/accounts/{accountId}/transfers")
    @ResponseStatus(HttpStatus.OK)
    public TransfersPage listTransfers(
            @PathVariable() UUID accountId,
            @RequestParam(defaultValue = "TRANSFERÊNCIA") TransactionType transactionType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) throws UnauthorizedException, ResourceNotFoundException {
        TransfersPage response = new TransfersPage();
        List<ITransferResponseDTO> transfers;
        Pageable pagging = PageRequest.of(page-1,pageSize);
        Page<ITransferResponseDTO> pageTransfers= service.listAllByAccountOriginAndTypeOrdByDate(accountId,transactionType,
                pagging);

        transfers = pageTransfers.getContent();

        response.setTransfers(transfers);
        response.setCurrentPage(pageTransfers.getNumber()+1);
        response.setTotalItems(pageTransfers.getTotalElements());
        response.setTotalPages(pageTransfers.getTotalPages());

//        Map<String, Object> response = new HashMap<>();
//        response.put("transfers",transfers);
//        response.put("currentPage",pageTransfers.getNumber()+1);
//        response.put("totalItems", pageTransfers.getTotalElements());
//        response.put("totalPages",pageTransfers.getTotalPages());
        return response;
    }

    @GetMapping("/accounts/{accountId}/transfers/recent")
    @ResponseStatus(HttpStatus.OK)
    public List<LastFiveTransfersDTO> listLastFiveRecentAccounts(@PathVariable UUID accountId) throws UnauthorizedException, ResourceNotFoundException {
        return service.listLastFiveAccountsTransferred(accountId);
    }
}
