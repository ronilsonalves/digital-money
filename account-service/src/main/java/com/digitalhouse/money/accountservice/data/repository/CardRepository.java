package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    public boolean existsByNumber(Long cardNumber);

    List<Card> findAllByAccountId(UUID accountId);
}
