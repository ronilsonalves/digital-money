package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.TransactionFilterRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.enums.ActivityType;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.response.PageResponse;
import com.digitalhouse.money.accountservice.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.convert.Delimiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Transactions")
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
        return service.save(requestDTO, accountId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponseDTO> listAllTransactionsByAccount(@PathVariable UUID accountId)
            throws UnauthorizedException, ResourceNotFoundException {
        return service.listAllTransactionsByAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/activity")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TransactionResponseDTO> getActivitiesByAccount(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) ActivityType activityType,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount
    )
            throws UnauthorizedException, ResourceNotFoundException, IllegalArgumentException {

        Pageable pageable = PageRequest.of(page, size, direction, sort);


        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before endDate");
        }

        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0) {
            throw new IllegalArgumentException("minAmount must be less than maxAmount");
        }

        TransactionFilterRequestDTO filter = TransactionFilterRequestDTO.builder()
                .transactionType(transactionType)
                .activityType(activityType)
                .startDate(startDate)
                .endDate(endDate)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .build();

        Page<TransactionResponseDTO> dtoPage = service.listActivitiesByAccount(
                accountId,
                pageable,
                filter
        );
        System.out.printf(dtoPage.getSort().toString());
        return PageResponse.<TransactionResponseDTO>builder()
                .data(dtoPage.getContent())
                .totalItems(dtoPage.getTotalElements())
                .totalPages(dtoPage.getTotalPages())
                .build();
    }

}
