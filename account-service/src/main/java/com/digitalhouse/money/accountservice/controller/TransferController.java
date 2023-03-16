package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.dto.TransferRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.ITransferResponseDTO;
import com.digitalhouse.money.accountservice.data.dto.TransferResponseDTO;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.InsufficientFundsException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
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
    public Map<String, Object> listTransfers(
            @PathVariable() UUID accountId,
            @RequestParam(defaultValue = "TRANSFERÊNCIA") TransactionType transactionType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) throws UnauthorizedException, ResourceNotFoundException {
        List<ITransferResponseDTO> transfers;
        Pageable pagging = PageRequest.of(page-1,pageSize);
        Page<ITransferResponseDTO> pageTransfers= service.listAllByAccountOriginAndTypeOrdByDate(accountId,transactionType,
                pagging);

        transfers = pageTransfers.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("transfers",transfers);
        response.put("currentPage",pageTransfers.getNumber()+1);
        response.put("totalItems", pageTransfers.getTotalElements());
        response.put("totalPages",pageTransfers.getTotalPages());
        return response;
    }
}
