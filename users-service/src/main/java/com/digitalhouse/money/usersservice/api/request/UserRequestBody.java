package com.digitalhouse.money.usersservice.api.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequestBody {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")
    @Size(min = 11, max = 14)
    private String cpf;

    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(\\(?[0-9]{2}\\)?)??([0-9]{4,5})-?([0-9]{4})$")
    @Size(min = 10, max = 15)
    private String phone;

    @NotBlank
    @Size(min=8, max = 32)
    private String password;

}
