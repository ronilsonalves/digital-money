package com.digitalhouse.money.usersservice.api.service;

import com.digitalhouse.money.usersservice.api.request.UpdateUserRequestBody;
import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.response.UserResponse;
import com.digitalhouse.money.usersservice.exceptionhandler.UnauthorizedException;

import java.util.UUID;

public interface UserService {

    UserResponse save(UserRequestBody userRequestBody);

    void resetPassword(String userEmailAddress);

    UserResponse getUserByUUID(UUID uuid) throws UnauthorizedException;

    UserResponse updateUser(UUID userUUID, UpdateUserRequestBody user);
}
