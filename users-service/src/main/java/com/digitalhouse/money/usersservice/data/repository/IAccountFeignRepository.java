package com.digitalhouse.money.usersservice.data.repository;

import com.digitalhouse.money.usersservice.api.request.AccountCreateRequestDTO;
import com.digitalhouse.money.usersservice.api.response.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "account-service")
public interface IAccountFeignRepository {

    @PostMapping("/api/accounts")
    AccountResponse createAccountWithUserID(@RequestBody AccountCreateRequestDTO createRequest);

    @GetMapping("/api/accounts/{account_id}")
    AccountResponse getAccountByAccountID(@PathVariable UUID account_id);

}
