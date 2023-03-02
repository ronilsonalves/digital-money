package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Card;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.CardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
@Tag(name = "Cards")
@SecurityRequirement(name = "BearerAuth")
public class CardController {

    @Autowired
    private CardService service;

    @PostMapping("/accounts/{accountId}/cards")
    public ResponseEntity<Card> createCard(@PathVariable UUID accountId, @Valid @RequestBody CardRequestDTO cardRequestDTO) {

        Card savedCard = service.save(cardRequestDTO,accountId);

        return new ResponseEntity(savedCard, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{accountId}/cards")
    public ResponseEntity<List<Card>> getCardsByAccountId(@PathVariable UUID accountId)
            throws UnauthorizedException, ResourceNotFoundException {
        return new ResponseEntity<>(service.getCardsByAccountId(accountId), HttpStatus.OK);
    }
}
