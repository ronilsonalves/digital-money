package com.digitalhouse.money.usersservice.api.request;

import com.digitalhouse.money.usersservice.api.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageDTO {
    @JsonProperty("user")
    private UserResponse userResponse;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("body")
    private String body;
}
