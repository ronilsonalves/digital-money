package com.digitalhouse.money.accountservice.util;

import com.digitalhouse.money.accountservice.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VerifyAuthenticationUtil {
    @Autowired
    AccountRepository accountRepository;

    public boolean isUserUUIDSameFromAuth(UUID requested) {
        return requested.equals(getLoggedUserUUID());
    }

    public boolean isLoggedUserOwnerOfAccount(UUID accountId) {
        return accountRepository.existsByIdAndUserId(accountId, getLoggedUserUUID());
    }

    public UUID getLoggedUserUUID() {
        return UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
    }
}
