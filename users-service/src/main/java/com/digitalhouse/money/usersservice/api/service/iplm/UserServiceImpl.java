package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.AccountCreateRequestDTO;
import com.digitalhouse.money.usersservice.api.request.UpdateUserRequestBody;
import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.response.Account;
import com.digitalhouse.money.usersservice.api.response.AccountResponse;
import com.digitalhouse.money.usersservice.api.response.UserResponse;
import com.digitalhouse.money.usersservice.api.service.UserService;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.data.repository.IAccountFeignRepository;
import com.digitalhouse.money.usersservice.data.repository.IUserKeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException;
import com.digitalhouse.money.usersservice.exceptionhandler.UnauthorizedException;
import jakarta.inject.Inject;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    private final IUserKeycloakRepository iUserKeycloak;

    private final UserRepository userRepository;

    private final IAccountFeignRepository accountRepository;

    @Inject
    public UserServiceImpl(IUserKeycloakRepository iUserKeycloak, UserRepository userRepository, IAccountFeignRepository accountRepository) {
        this.iUserKeycloak = iUserKeycloak;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public UserResponse save(UserRequestBody userRequestBody) {
        try {
            User fromKeyCloak = iUserKeycloak.save(userRequestBody);
            BeanUtils.copyProperties(userRequestBody,fromKeyCloak);
            AccountResponse account =
                    accountRepository.createAccountWithUserID(new AccountCreateRequestDTO(fromKeyCloak.getId()));
            fromKeyCloak.setAccountNumber(account.getId());
            User saved = userRepository.save(fromKeyCloak);
            return new UserResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getLastName(),
                    saved.getCpf(),
                    saved.getEmail(),
                    saved.getPhone(),
                    new Account(account.getAvailable_amount(),saved.getAccountNumber())
            );
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new BadRequestException(e.toString());
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
    public UserResponse getUserByUUID(UUID uuid) throws ResourceNotFoundException, UnauthorizedException {
        User fromKeycloak = iUserKeycloak.findUserByUUID(uuid);
        if (!isUserUUIDSameFromAuth(uuid))
            throw new UnauthorizedException("User not authorized to perform this request");
        if (userRepository.existsById(uuid) && fromKeycloak != null) {
            UserResponse response = new UserResponse();
            User fromDb =
                    userRepository.findById(uuid).orElseThrow( () -> new ResourceNotFoundException(
                            "There's no user registered in db with this UUID: "+uuid
                    )
            );
            logger.warning("AccountNumber in User: "+fromDb.getAccountNumber());
            AccountResponse account = accountRepository.getAccountByAccountID(fromDb.getAccountNumber());
            BeanUtils.copyProperties(fromDb,response);
            response.setAccount(new Account(account.getAvailable_amount(),fromDb.getAccountNumber()));
            return response;
        } else throw new ResourceNotFoundException("There's no user registered with UUID: "+uuid);
    }

    @Override
    public UserResponse updateUser(UUID userUUID, UpdateUserRequestBody user) {
        Optional<User> fromDb;
        fromDb = userRepository.findById(userUUID);
        if (!isUserUUIDSameFromAuth(userUUID))
            throw new UnauthorizedException("User not authorized to perform this request");
        if (userRepository.existsById(userUUID) && fromDb.isPresent()) {
            UUID accountNumber = fromDb.get().getAccountNumber();
            UserRequestBody toKeycloak = new UserRequestBody();
            //Validating if any field is empty and setting CPF for already present in DB
            if (user.getName() == null)
                toKeycloak.setName(fromDb.get().getName());
            if (user.getLastName() == null)
                toKeycloak.setLastName(fromDb.get().getLastName());
            if (user.getEmail() == null)
                toKeycloak.setEmail(fromDb.get().getEmail());
            User resultFromKeycloak = iUserKeycloak.updateUser(userUUID,toKeycloak);

            if (user.getPhone() != null) {
                resultFromKeycloak.setPhone(user.getPhone());
            } else {
                resultFromKeycloak.setPhone(fromDb.get().getPhone());
            }
            resultFromKeycloak.setCpf(fromDb.get().getCpf());
            resultFromKeycloak.setAccountNumber(accountNumber);
            User updated = userRepository.save(resultFromKeycloak);
            AccountResponse account = accountRepository.getAccountByAccountID(updated.getAccountNumber());
            return new UserResponse(
                    updated.getId(),
                    updated.getName(),
                    updated.getLastName(),
                    updated.getCpf(),
                    updated.getEmail(),
                    updated.getPhone(),
                    new Account(account.getAvailable_amount(),updated.getAccountNumber())
            );
        } else throw new ResourceNotFoundException("There's no user in db with UUID: "+userUUID);
    }


    private boolean isUserUUIDSameFromAuth(UUID requested) {
        UUID fromAuthContext = UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
        return fromAuthContext.equals(requested);
    }
}
