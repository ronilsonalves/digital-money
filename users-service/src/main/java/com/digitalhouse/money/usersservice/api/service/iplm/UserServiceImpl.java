package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.data.repository.IUserKeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException;
import jakarta.inject.Inject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
            User fromKeyCloak = iUserKeycloak.save(userRequestBody);
            BeanUtils.copyProperties(userRequestBody,fromKeyCloak);
            return userRepository.save(fromKeyCloak);
        } catch (Exception e) {
            logger.info(e.toString());
            throw new BadRequestException(e.getMessage());
        }
    }

    public void resetPassword(String userEmailAddress) {
        Optional<User> fromDb = userRepository.findUserByEmail(userEmailAddress);
        if (fromDb.isPresent()) {
            try {
                iUserKeycloak.resetPassword(fromDb.get().getId());
            } catch (BadRequestException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        if (fromDb.isEmpty()) {
            throw new ResourceNotFoundException("There's no user registered with the email address provided");
        }
    }

    @Override
    public User getUserByUUID(UUID uuid) throws ResourceNotFoundException {
        return iUserKeycloak.findUserByUUID(uuid);
    }
}
