package com.digitalhouse.money.usersservice.api.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TokenInvalidationService {

    private static final Logger log = Logger.getLogger(TokenInvalidationService.class.getName());

    @Value("${digitalmoney.keycloak.serverUrl}")
    private String serverURL;
    @Value("${digitalmoney.keycloak.clientIdUsers}")
    private String clientID;
    @Value("${digitalmoney.keycloak.clientSecretUsers}")
    private String clientSecret;
    @Value("${digitalmoney.keycloak.realm}")
    private String realm;


    /**
     * Invalidates a token to logout user
     * @param token
     */
    public void invalidateToken(String token) {
        URI url = URI.create(serverURL+"realms/"+realm+"/protocol/openid-connect/revoke");
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientID);
        map.add("client_secret",clientSecret);
        map.add("token",token);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,null);
        restTemplate.exchange(url, HttpMethod.POST,request,String.class);
    }
}
