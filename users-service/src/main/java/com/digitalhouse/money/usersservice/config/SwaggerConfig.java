package com.digitalhouse.money.usersservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Users API")
                        .description("Users API for Digital Money")
                        .version("0.0.1")
                        .contact(new Contact().name("Digital Money")
                                .email("hello@digitalhouse.com")
                                .url("https://digitalhouse.com"))
                        .license(new License().name("MIT License").url("https://digitalhouse.com")));
    }

    @Bean
    public GroupedOpenApi usersAPI() {
        return GroupedOpenApi.builder()
                .group("Users")
                .displayName("Users API")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authAPI() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .displayName("Auth")
                .pathsToMatch("/auth/**")
                .build();
    }
}
