package com.digitalhouse.money.usersservice.data.repository;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.data.model.User;

import java.util.UUID;

public interface IUserKeycloakRepository {
    User findUserByUUID(UUID uuid);

    void resetPassword(UUID userID);

    User save(UserRequestBody user);

    User updateUser(UUID userUUID, UserRequestBody user);

    void verifyEmailAddress(UUID userID);

    void logout(String token);
}
