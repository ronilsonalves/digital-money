package com.digitalhouse.money.usersservice.api.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String balance;
    private UUID accountNumber;
}
