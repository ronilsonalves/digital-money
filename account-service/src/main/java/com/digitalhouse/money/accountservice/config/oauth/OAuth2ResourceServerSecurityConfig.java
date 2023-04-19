package com.digitalhouse.money.accountservice.config.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class OAuth2ResourceServerSecurityConfig {

    @Value("${KEYCLOAK_SERVER_URL}")
    private String keycloakServerURL;

    @Value("${digitalmoney.keycloak.realm}")
    private String realm;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                authorize -> {
                    try {
                        authorize
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/swagger/**",
                                        "/webjars/**",
                                        "/v3/api-docs/**",
                                        "/actuator/**")
                                .permitAll()
                                .and().sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and().oauth2ResourceServer()
                                .jwt()
                                .jwtAuthenticationConverter(new KeycloakJwtAuthConverter());
                                authorize
                                        .anyRequest().authenticated()
                                        .and().cors().and().csrf().disable();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return httpSecurity.build();
    };

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(keycloakServerURL+"realms/"+realm);
    }
}
