package com.digitalhouse.money.usersservice.api.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class KeycloakUserInfoResponse {
    private String sub;
    private boolean emailVerified;
    private String name;
    private String email;
}
