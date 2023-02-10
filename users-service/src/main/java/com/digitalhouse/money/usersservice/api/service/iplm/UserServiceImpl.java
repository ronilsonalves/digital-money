package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.data.repository.IUserKeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import jakarta.inject.Inject;
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
        try {
            logger.info(userRequestBody.toString());
            User fromKeyCloak = iUserKeycloak.save(userRequestBody);
            BeanUtils.copyProperties(userRequestBody,fromKeyCloak);
            logger.info(fromKeyCloak.toString());
            return userRepository.save(fromKeyCloak);
        } catch (Exception e) {
            logger.info(e.toString());
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    public User getUserByUUID(UUID uuid) {
        return iUserKeycloak.findUserByUUID(uuid);
    }
}
