package com.digitalhouse.money.accountservice.data.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String lastName,
        String email
) {
}
