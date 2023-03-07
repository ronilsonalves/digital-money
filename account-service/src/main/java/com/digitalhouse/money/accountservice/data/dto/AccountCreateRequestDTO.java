package com.digitalhouse.money.accountservice.data.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequestDTO {
    private UUID userId;
}
