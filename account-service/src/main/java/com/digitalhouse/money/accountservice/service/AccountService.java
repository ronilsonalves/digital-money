package com.digitalhouse.money.accountservice.service;

import com.digitalhouse.money.accountservice.data.model.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface AccountService {
    Account save(Account any);

    Account getById(UUID account_id);
}
