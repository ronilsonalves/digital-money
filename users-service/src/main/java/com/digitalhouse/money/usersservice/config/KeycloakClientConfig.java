package com.digitalhouse.money.usersservice.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakClientConfig {
    @Value("${digitalmoney.keycloak.realm}")
    private String realm;

    @Value("${digitalmoney.keycloak.serverUrl}")
    private String serverUrl;

    @Value("${digitalmoney.keycloak.clientid}")
    private String clientId;

    @Value("${digitalmoney.keycloak.clientsecret}")
    private String clientSecret;

    @Bean
    public Keycloak getInstance() {
        ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilderImpl();
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient(resteasyClientBuilder.connectionPoolSize(10).build())
                .build();
    }
}
