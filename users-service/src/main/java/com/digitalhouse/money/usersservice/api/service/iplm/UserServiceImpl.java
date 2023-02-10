package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.data.repository.IUserKeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    private final IUserKeycloakRepository iUserKeycloak;

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(IUserKeycloakRepository iUserKeycloak, UserRepository userRepository) {
        this.iUserKeycloak = iUserKeycloak;
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRequestBody userRequestBody) {
        logger.info(userRequestBody.toString());
        User fromKeyCloak = iUserKeycloak.save(userRequestBody);
        logger.info(fromKeyCloak.toString());
        BeanUtils.copyProperties(userRequestBody,fromKeyCloak);
        return userRepository.save(fromKeyCloak);
    }

    @Override
    public User getUserByUUID(UUID uuid) {
        return iUserKeycloak.findUserByUUID(uuid);
    }
}
