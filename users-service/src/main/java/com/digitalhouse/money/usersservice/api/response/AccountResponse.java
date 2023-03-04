package com.digitalhouse.money.usersservice.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class AccountResponse {
    //ToDo: verificar com os colegas a utilização de camelCase
    private String available_amount;
    private UUID id;
    private UUID user_id;
}
