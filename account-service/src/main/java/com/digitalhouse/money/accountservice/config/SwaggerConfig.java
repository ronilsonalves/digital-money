package com.digitalhouse.money.accountservice.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "BearerAuth",  scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Account's API")
                        .description("Account's API for Digital Money")
                        .version("0.0.1")
                        .contact(new Contact().name("Digital Money")
                                .email("hello@digitalhouse.com")
                                .url("https://digitalhouse.com"))
                        .license(new License().name("MIT License").url("https://digitalhouse.com")));
    }

    @Bean
    public GroupedOpenApi accountsAPI() {
        return GroupedOpenApi.builder()
                .group("Accounts")
                .displayName("Account's API")
                .pathsToMatch("/api/accounts/**")
                .build();
    }

}
