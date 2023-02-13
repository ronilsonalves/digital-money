package com.digitalhouse.money.usersservice.api.service;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.data.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface UserService {

    User save(UserRequestBody userRequestBody);

    void resetPassword(String userEmailAddress);

    User getUserByUUID(UUID uuid);
}
