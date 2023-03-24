package com.digitalhouse.money.accountservice.data.dto;

import com.digitalhouse.money.accountservice.data.model.Transaction;
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
    @JsonProperty("transaction")
    private Transaction transaction;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("body")
    private String body;
}
