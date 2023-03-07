package com.digitalhouse.money.usersservice.api.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class UpdateUserRequestBody {

    private String name;

    private String lastName;

    @Email
    private String email;

    @Pattern(regexp = "^(\\(?[0-9]{2}\\)?)??([0-9]{4,5})-?([0-9]{4})$")
    @Size(min = 10, max = 15)
    private String phone;

    @Size(min=8, max = 32)
    private String password;

}
