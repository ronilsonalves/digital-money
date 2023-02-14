package com.digitalhouse.money.usersservice.api.controller;

import com.digitalhouse.money.usersservice.api.request.UserLoginRequestBody;
import com.digitalhouse.money.usersservice.api.response.TokenResponse;
import com.digitalhouse.money.usersservice.api.service.auth.AuthenticationService;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.usersservice.exceptionhandler.InvalidCredentialsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequestBody userLoginRequestBody) throws BadRequestException, InvalidCredentialsException {
        return ResponseEntity.ok().body(authenticationService.login(userLoginRequestBody));
    }
}
