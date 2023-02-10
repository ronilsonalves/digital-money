package com.digitalhouse.money.usersservice.api.controller;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final Logger logger = Logger.getLogger(UserController.class.getName());

    //ToDo: Implement getAll users
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        logger.info("/users");
        List<User> retorno = new ArrayList<>();
        return ResponseEntity.status(HttpStatus.OK).body(retorno);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestBody userRequestBody) {
        logger.info(userRequestBody.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestBody));
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<User> getUserByUUID(@PathVariable UUID userUUID) {
        logger.info(userUUID.toString());
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUUID(userUUID));
    }
}
