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
import com.digitalhouse.money.accountservice.util.MailConstructor;
import com.digitalhouse.money.accountservice.util.VerifyAuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository repository;
    private final AccountRepository accountRepository;
    private final VerifyAuthenticationUtil verifyAuthenticationUtil;
    private final RabbitTemplate rabbitTemplate;
    private final MailConstructor mailConstructor;

    @Override
    public Card save(CardRequestDTO dto, UUID account_id) {

        Account account = accountRepository.findById(account_id
        ).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (repository.existsByNumber(dto.getNumber())) {
            throw new ConflictException("Cartão já esteja associado a uma conta");
        }

        if (dto.getNumber().toString().length() != 16){
            throw new BadRequestException("Invalid card number size");
        }

        if (!verifyAuthenticationUtil.isUserUUIDSameFromAuth(account.getUserId())){
            throw new UnauthorizedException("User not authorized");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expirationDate = YearMonth.parse(dto.getExpiration_date(), formatter);
            if (expirationDate.isBefore(YearMonth.now())) {
                throw new ConflictException("Date invalid");
            }
            Card card = repository.save(Card.builder()
                    .accountId(account_id)
                    .expirationDate(expirationDate.toString())
                    .cvc(dto.getCvc())
                    .firstLastName(dto.getFirst_last_name())
                    .number(dto.getNumber())
                    .build());
            rabbitTemplate.convertAndSend("mail-service",mailConstructor.getMailMessageAddCard(account,
                    card.getNumber().toString().substring(12),card.getId(),card.getFirstLastName()));
            return card;
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

        return repository.findAllByAccountId(accountId);
    }

    @Override
    public void delete(UUID accountId, UUID id) {

        Card card = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        Account account = accountRepository.findByUserId(UUID.fromString(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        )).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.getId().equals(accountId) || !card.getAccountId().equals(accountId)) {
            throw new UnauthorizedException("User not authorized");
        }

        repository.deleteById(id);
        rabbitTemplate.convertAndSend("mail-service",mailConstructor.getMailMessageDelCard(account,
                card.getNumber().toString().substring(12),card.getId(), card.getFirstLastName()));
    }
}
