package com.digitalhouse.money.usersservice.config.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class OAuth2ResourceServerSecurityConfig {

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
                                        "/actuator/**",
                                        "/auth/login",
                                        "/auth/logout",
                                        "/users/register",
                                        "/users/reset-password/**",
                                        "/users/send-verification/**",
                                        "/users/verify-email/**")
                                .permitAll()
                                .and().oauth2ResourceServer()
                                .jwt()
                                .jwtAuthenticationConverter(new KeycloakJwtAuthConverter());
                        authorize
                                .anyRequest().authenticated()
                                .and().cors().disable().csrf().disable();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return httpSecurity.build();
    };

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://auth.pi.ronilsonalves.com/realms/digitalmoney");
    }
}
