package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.AccountCreateRequestDTO;
import com.digitalhouse.money.usersservice.api.request.UpdateUserRequestBody;
import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
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
import com.digitalhouse.money.usersservice.util.MailConstructor;
import jakarta.inject.Inject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    private final IUserKeycloakRepository iUserKeycloak;

    private final UserRepository userRepository;

    private final IAccountFeignRepository accountRepository;

    private final RabbitTemplate rabbitTemplate;
    private final MailConstructor mailConstructor;

    @Inject
    public UserServiceImpl(IUserKeycloakRepository iUserKeycloak, UserRepository userRepository,
                           IAccountFeignRepository accountRepository, MailConstructor mailConstructor,
                           RabbitTemplate rabbitTemplate) {
        this.iUserKeycloak = iUserKeycloak;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.mailConstructor = mailConstructor;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserResponse save(UserRequestBody userRequestBody) {
        try {
            User fromKeyCloak = iUserKeycloak.save(userRequestBody);
            BeanUtils.copyProperties(userRequestBody,fromKeyCloak);
            AccountResponse account =
                    accountRepository.createAccountWithUserID(new AccountCreateRequestDTO(fromKeyCloak.getId()));
            fromKeyCloak.setAccountNumber(account.getId());
            fromKeyCloak.setEmailVerificationCode(String.valueOf(new Random().nextInt(999999)));
            User saved = userRepository.save(fromKeyCloak);
            UserResponse response = new UserResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getLastName(),
                    saved.getCpf(),
                    saved.getEmail(),
                    saved.getPhone(),
                    saved.getAccountNumber()
            );
            rabbitTemplate.convertAndSend("mail-service",mailConstructor.getMailMessageUserRegistered(response,
                    saved.getEmailVerificationCode()));
            return response;
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
            BeanUtils.copyProperties(fromDb,response);
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

            if (!resultFromKeycloak.getEmail().equals(fromDb.get().getEmail()))
                resultFromKeycloak.setEmailVerificationCode(String.valueOf(new Random().nextInt(999999)));

            if (user.getPhone() != null) {
                resultFromKeycloak.setPhone(user.getPhone());
            } else {
                resultFromKeycloak.setPhone(fromDb.get().getPhone());
            }
            resultFromKeycloak.setCpf(fromDb.get().getCpf());
            resultFromKeycloak.setAccountNumber(accountNumber);
            User updated = userRepository.save(resultFromKeycloak);
            UserResponse response = new UserResponse(
                    updated.getId(),
                    updated.getName(),
                    updated.getLastName(),
                    updated.getCpf(),
                    updated.getEmail(),
                    updated.getPhone(),
                    updated.getAccountNumber()
            );
            if (updated.getEmailVerificationCode()!=null) {
                rabbitTemplate.convertAndSend("mail-service",mailConstructor.getEmailMessageChangedEmail(response,
                        updated.getEmailVerificationCode()));
            }
            return response;
        } else throw new ResourceNotFoundException("There's no user in db with UUID: "+userUUID);
    }


    @Override
    public void resendEmailVerifyCode(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail).orElseThrow(()->
                new ResourceNotFoundException("There's no user registered with email address provided!"));
        user.setEmailVerificationCode(String.valueOf(new Random().nextInt(999999)));
        userRepository.save(user);
        rabbitTemplate.convertAndSend("mail-service",
                mailConstructor.getMailMessageResendCode(UserResponse.builder()
                        .name(user.getName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .build(), user.getEmailVerificationCode()));
    }

    @Override
    public void verifyEmail(String verificationCode) {
        User user =
                userRepository.findUserByEmailVerificationCode(verificationCode.trim()).orElseThrow(() ->
                        new BadRequestException("The user already verified or verification code has been expired"));
        iUserKeycloak.verifyEmailAddress(user.getId());
        user.setEmailVerificationCode(null);
        userRepository.save(user);
        rabbitTemplate.convertAndSend("mail-service",
                mailConstructor.getEmailMessageEmailVerified(UserResponse.builder()
                        .name(user.getName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .build()));
    }


    private boolean isUserUUIDSameFromAuth(UUID requested) {
        UUID fromAuthContext = UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
        //if service is requesting this info is a back-end client return is true
        if (fromAuthContext.equals(UUID.fromString("75791a12-a3ec-497a-b426-0888fc83ba6a")))
            return true;
        return fromAuthContext.equals(requested);
    }
}
