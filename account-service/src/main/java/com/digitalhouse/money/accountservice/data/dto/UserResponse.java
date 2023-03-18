package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.model.Account;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String lastName,
        Account account
) {
}
