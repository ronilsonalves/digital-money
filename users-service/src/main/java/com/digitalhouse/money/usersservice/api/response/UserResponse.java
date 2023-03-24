package com.digitalhouse.money.usersservice.api.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String lastName;
    private String cpf;
    private String email;
    private String phone;
    private UUID accountNumber;
}
