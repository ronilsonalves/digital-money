package com.digitalhouse.money.accountservice.controller;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Card;
import com.digitalhouse.money.accountservice.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class CardController {

    @Autowired
    private CardService service;

    @PostMapping("/accounts/{account_id}/cards")
    public ResponseEntity<Card> createCard(@PathVariable UUID account_id, @Valid @RequestBody CardRequestDTO cardRequestDTO) {

        Card savedCard = service.save(cardRequestDTO,account_id);

        return new ResponseEntity(savedCard, HttpStatus.CREATED);
    }

}
