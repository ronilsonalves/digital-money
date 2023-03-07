package com.digitalhouse.money.usersservice.api.response;

import com.digitalhouse.money.usersservice.data.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String lastName;
    private String cpf;
    private String email;
    private String phone;
    private Account account;
}
