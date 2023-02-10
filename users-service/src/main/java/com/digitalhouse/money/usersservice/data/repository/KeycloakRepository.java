package com.digitalhouse.money.usersservice.data.repository;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.data.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
@RequiredArgsConstructor
public class KeycloakRepository implements IUserKeycloakRepository {

    private static Logger log = Logger.getLogger(KeycloakRepository.class.getName());

    private final Keycloak keycloak;

    @Value("${digitalmoney.keycloak.realm}")
    private String realm;

    @Override
    public User findUserByUUID(UUID uuid) {
        try {
            UserResource userResource = keycloak
                    .realm(realm)
                    .users().get(String.valueOf(uuid));

            UserRepresentation userRepresentation = userResource.toRepresentation();
            return fromRepresentation(userRepresentation);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return null;
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
            return null;
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
        userRepresentation.setEnabled(false);
        return userRepresentation;
    }
}
