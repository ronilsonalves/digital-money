package com.digitalhouse.money.accountservice.service;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardService {

    Card save(CardRequestDTO card, UUID account_id);

    Optional<Card> getById(UUID id);

    List<Card> getCardsByAccountId(UUID accountId);

    void delete(Card card);
}
