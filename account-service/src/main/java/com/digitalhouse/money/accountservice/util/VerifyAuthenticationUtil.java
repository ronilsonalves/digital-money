package com.digitalhouse.money.accountservice.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VerifyAuthenticationUtil {

    public boolean isUserUUIDSameFromAuth(UUID requested) {
        UUID fromAuthContext = UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
        return fromAuthContext.equals(requested);
    }
}
