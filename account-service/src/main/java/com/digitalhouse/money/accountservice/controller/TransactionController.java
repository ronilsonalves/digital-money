package com.digitalhouse.money.accountservice.controller;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.digitalhouse.money.accountservice.data.dto.TransactionFilterRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionRequestDTO;
import com.digitalhouse.money.accountservice.data.dto.TransactionResponseDTO;
import com.digitalhouse.money.accountservice.data.enums.ActivityType;
import com.digitalhouse.money.accountservice.data.enums.TransactionType;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.response.PageResponse;
import com.digitalhouse.money.accountservice.service.PdfGenUploadService;
import com.digitalhouse.money.accountservice.service.TransactionService;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Transactions")
@SecurityRequirement(name = "BearerAuth")
public class TransactionController {

    private final TransactionService service;
    private final PdfGenUploadService pdfService;


    public TransactionController(TransactionService service, PdfGenUploadService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    @PostMapping("/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO createTransaction(@PathVariable UUID accountId,
                                                    @RequestBody TransactionRequestDTO requestDTO
    ) throws UnauthorizedException, ResourceNotFoundException, DocumentException {
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

    @GetMapping("/accounts/{accountId}/activity/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponseDTO getTransactionById(@PathVariable @NotNull UUID accountId, @PathVariable @NotNull UUID transactionId)
            throws UnauthorizedException, ResourceNotFoundException, BadRequestException {

        return service.getTransactionById(accountId, transactionId);
    }

//    @GetMapping("/accounts/{accountId}/activity/{transactionId}/receipt")
//    @Produces(value = "application/pdf")
//    public void downloadPdf(@PathVariable UUID accountId, @PathVariable UUID transactionId,
//                              HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("application/pdf");
//        try (InputStream inputStream = pdfService.getPdfS3Url(accountId,transactionId).openStream()) {
//            IOUtils.copy(inputStream, response.getOutputStream());
//            response.flushBuffer();
//        } catch (IOException e) {
//            throw new BadRequestException("A error occurred while trying to fetch your receipt.");
//        }
//    }

    @GetMapping(value = "/accounts/{accountId}/activity/{transactionId}/receipt", produces =
            {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public StreamingResponseBody downloadReceiptFile(@PathVariable @NotNull UUID accountId,
                                                     @PathVariable @NotNull UUID transactionId) throws ResourceNotFoundException, UnauthorizedException {

        S3ObjectInputStream finalObject = pdfService.getS3Object(accountId,transactionId);
        final StreamingResponseBody body = outputStream -> {
            int bytesToWrite = 0;
            byte[] data = new byte[1024];
            while ((bytesToWrite = finalObject.read(data,0, data.length)) != -1) {
                outputStream.write(data,0,bytesToWrite);
            }
            finalObject.close();
        };
        return body;
    }

}
