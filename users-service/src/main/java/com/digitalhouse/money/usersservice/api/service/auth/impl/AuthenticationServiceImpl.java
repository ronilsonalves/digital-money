package com.digitalhouse.money.usersservice.api.service.auth.impl;

import com.digitalhouse.money.usersservice.api.request.UserLoginRequestBody;
import com.digitalhouse.money.usersservice.api.response.TokenResponse;
import com.digitalhouse.money.usersservice.api.service.auth.AuthenticationService;
import com.digitalhouse.money.usersservice.data.repository.KeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final KeycloakRepository keycloakRepository;
    private final UserRepository userRepository;

    @Override
    public TokenResponse login(UserLoginRequestBody userLoginRequestBody) {
        if (userRepository.findUserByEmail(userLoginRequestBody.getEmail()).isPresent()) {
            Object tokenObject = keycloakRepository.login(userLoginRequestBody);
            TokenResponse tokenResponse = new TokenResponse();
            BeanUtils.copyProperties(tokenObject,tokenResponse);
            return tokenResponse;
        }
        throw new ResourceNotFoundException("There's no user with email address informed");
    }

    @Override
    public void logout(String id, String token) {
        keycloakRepository.logout(id, token);
    }
}
