package com.digitalhouse.money.accountservice.service.impl;

import com.digitalhouse.money.accountservice.data.dto.CardRequestDTO;
import com.digitalhouse.money.accountservice.data.model.Account;
import com.digitalhouse.money.accountservice.data.model.Card;
import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import com.digitalhouse.money.accountservice.data.repository.CardRepository;
import com.digitalhouse.money.accountservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.accountservice.exceptionhandler.ConflictException;
import com.digitalhouse.money.accountservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.accountservice.exceptionhandler.UnauthorizedException;
import com.digitalhouse.money.accountservice.service.CardService;
import com.digitalhouse.money.accountservice.util.VerifyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository repository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    VerifyAuthenticationUtil verifyAuthenticationUtil;

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
    public Card getById(UUID accountId, UUID id) {
        List<Card> accountCards = getCardsByAccountId(accountId);

        return accountCards.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }

    @Override
    public List<Card> getCardsByAccountId(UUID accountId) {

        Optional<Account> account = accountRepository.findByUserId(UUID.fromString(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        ));

        if (!(accountRepository.existsById(accountId))) {
            throw new ResourceNotFoundException("Account not found");
        }

        if (account.isPresent()) {

            if (!account.get().getId().equals(accountId)) {
                throw new UnauthorizedException("User not authorized");
            }

        }

        return cardRepository.findAllByAccountId(accountId);
    }

    @Override
    public void delete(UUID accountId, UUID id) {

        Account account = accountRepository.findByUserId(UUID.fromString(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        )).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getId().equals(accountId)) {
                throw new UnauthorizedException("User not authorized");
            }

        if (!cardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Card not found");
        }

        repository.deleteById(id);
    }
}
