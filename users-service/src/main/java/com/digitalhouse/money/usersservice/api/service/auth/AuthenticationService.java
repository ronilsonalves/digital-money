package com.digitalhouse.money.usersservice.api.service.auth;

import com.digitalhouse.money.usersservice.api.request.UserLoginRequestBody;
import com.digitalhouse.money.usersservice.api.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse login(UserLoginRequestBody userLoginRequestBody);

    void logout(String id, String token);
}
