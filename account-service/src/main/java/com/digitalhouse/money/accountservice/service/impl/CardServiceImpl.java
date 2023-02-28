package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Card;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.data.repository.CardRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository repository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Card save(CardRequestDTO dto, UUID account_id) {

        if (repository.existsByNumber(dto.getNumber())) {
            throw new ConflictException("Cartão já esteja associado a uma conta");
        }

        if (!accountRepository.existsById(account_id)) {
            throw new BadRequestException("Invalid Account ");
        }

        if (dto.getNumber().toString().length() != 16){
            throw new BadRequestException("Invalid card number size");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            YearMonth expirationDate = YearMonth.parse(dto.getExpiration_date(), formatter);
            if (expirationDate.isBefore(YearMonth.now())) {
                throw new ConflictException("Date invalid");
            }
            Card card = Card.builder()
                    .accountId(account_id)
                    .expirationDate(expirationDate.toString())
                    .cvc(dto.getCvc())
                    .firstLastName(dto.getFirst_last_name())
                    .number(dto.getNumber())
                    .build();

            return repository.save(card);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }

    }

    @Override
    public Optional<Card> getById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Card[]> getByUserId(UUID userId) {
        return Optional.empty();
    }

    @Override
    public void delete(Card card) {

    }


}
