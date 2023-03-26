package com.digitalhouse.money.usersservice.util;

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
        //if service is requesting this info is a back-end client return is true
        if (fromAuthContext.equals(UUID.fromString("75791a12-a3ec-497a-b426-0888fc83ba6a")))
            return true;
        return fromAuthContext.equals(requested);
    }
}
