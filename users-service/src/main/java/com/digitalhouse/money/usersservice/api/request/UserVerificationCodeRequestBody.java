package com.digitalhouse.money.usersservice.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserVerificationCodeRequestBody {

    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^\\d{6}$")
    private String code;
}
