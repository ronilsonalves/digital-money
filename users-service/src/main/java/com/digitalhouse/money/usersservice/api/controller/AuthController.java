package com.digitalhouse.money.usersservice.api.controller;

import com.digitalhouse.money.usersservice.api.request.UserLoginRequestBody;
import com.digitalhouse.money.usersservice.api.response.TokenResponse;
import com.digitalhouse.money.usersservice.api.service.auth.AuthenticationService;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.usersservice.exceptionhandler.InvalidCredentialsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable @NotBlank String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token)
            throws BadRequestException, InvalidCredentialsException {
        authenticationService.logout(id, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
