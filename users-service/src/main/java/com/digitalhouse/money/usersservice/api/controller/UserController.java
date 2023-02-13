package com.digitalhouse.money.usersservice.api.controller;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.request.UserResetPasswordRequest;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;


    /**
     * Register a new user into keycloak server and users-service's database
     * @param userRequestBody with all necessary fields to register a new user
     * @return User object containing all fields from userRequestBody excepting the password
     * @throws com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException if there's any invalid field in
     * userRequestBody payload.
     */
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserRequestBody userRequestBody) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestBody));
    }

    /**
     * Create a request to reset password sent to user's email
     * @param userResetPasswordRequest provided in request body
     * @return HttpStatus.OK if there's a user registered with the email address provided
     * @throws BadRequestException if an invalid email address is provided
     * @throws ResourceNotFoundException if a valid email address is provided but there's no user with this email
     * registered
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordRequest userResetPasswordRequest) {
        userService.resetPassword(userResetPasswordRequest.getEmailAddress());
        return ResponseEntity.status(HttpStatus.OK).body("If there's an user with email address provided a message " +
                "with instructions to reset your password will be sent to your inbox.");
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<User> getUserByUUID(@PathVariable UUID userUUID) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUUID(userUUID));
    }
}
