package com.digitalhouse.money.usersservice.data.repository;

import com.digitalhouse.money.usersservice.api.request.UserLoginRequestBody;
import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.api.response.TokenResponse;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.exceptionhandler.InvalidCredentialsException;
import com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class KeycloakRepository implements IUserKeycloakRepository {

    private static Logger log = Logger.getLogger(KeycloakRepository.class.getName());

    private final Keycloak keycloak;

    @Value("${digitalmoney.keycloak.realm}")
    private String realm;

    @Value("${digitalmoney.keycloak.clientIdUsers}")
    private String clientId;

    @Value("${digitalmoney.keycloak.clientSecretUsers}")
    private String clientSecret;

    @Override
    public User findUserByUUID(UUID uuid) throws NotFoundException{
        try {
            UserResource userResource = keycloak
                    .realm(realm)
                    .users().get(String.valueOf(uuid));

            UserRepresentation userRepresentation = userResource.toRepresentation();
            return fromRepresentation(userRepresentation);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("There's no user with data provided registered");
        }
    }

    @Override
    public User save(UserRequestBody user) {
        try {
            UserRepresentation userRepresentation = fromUser(user);
            Response userResponse = keycloak
                    .realm(realm).users().create(userRepresentation);
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            String userId = CreatedResponseUtil.getCreatedId(userResponse);
            passwordCred.setTemporary(false);
            passwordCred.setType("password");
            passwordCred.setValue(user.getPassword());
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            userResource.resetPassword(passwordCred);
            User response = new User();
            response.setId(UUID.fromString(userId));
            userResponse.close();
            return response;
        } catch (BadRequestException e) {
            log.info(e.getMessage());
            throw new com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException(
                    "Please, verify the provided data for fields on RequestBody and try again.");
        }
    }

    /**
     * Send an email message through Keycloak to user reset your password.
     * @param userID provided by query into users-service's database.
     * @throws ResourceNotFoundException if there's no user registered in Keycloak with provided UUID
     * @throws com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException if an invalid email address
     * or any other param(s) provided is invalid, this can be interfered by Realm configs and if a user is
     * disabled.
     */
    @Override
    public void resetPassword(UUID userID) {
        try {
            keycloak.realm(realm).users().get(String.valueOf(userID)).executeActionsEmail(Collections.singletonList(
                    "UPDATE_PASSWORD"));
        } catch (NotFoundException e) {
            throw new com.digitalhouse.money.usersservice.exceptionhandler.ResourceNotFoundException("There's no user " +
                    "registered with provided email address.");
        }
        catch (BadRequestException e) {
            throw new com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException("An error occurred " +
                    "while trying to perform your request, try again later and if error persists contact support. ");
        }
    }

    public Object login(UserLoginRequestBody userLoginRequestBody) {
        try {
            Keycloak keycloak1 = Keycloak.getInstance("http://localhost:8070/",realm,
                    userLoginRequestBody.getEmail(), userLoginRequestBody.getPassword(),clientId,
                    clientSecret);

            return new TokenResponse("Bearer "+keycloak1.tokenManager().getAccessToken().getToken());
        } catch (BadRequestException e) {
            throw new com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException("An error occurred " +
                    "while trying to authenticate user.");
        } catch (NotAuthorizedException e) {
            throw new InvalidCredentialsException("User and/or pass are invalid! Please check the credentials and try again.");
        }
    }

    private User fromRepresentation(UserRepresentation userRepresentation) {
        return new User(userRepresentation.getId(), userRepresentation.getFirstName(),
                userRepresentation.getLastName(), userRepresentation.getEmail());
    }

    private UserRepresentation fromUser(UserRequestBody user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getCpf());
        userRepresentation.setFirstName(user.getName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }
}
