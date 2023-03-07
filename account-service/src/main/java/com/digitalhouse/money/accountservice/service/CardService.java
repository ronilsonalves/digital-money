package com.digitalhouse.money.accountservice.service;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardService {

    Card save(CardRequestDTO card, UUID accountId);

    Card getById(UUID accountId, UUID id);

    List<Card> getCardsByAccountId(UUID accountId);

    void delete(UUID accountId, UUID id);
}
