package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
     boolean existsByUserId(UUID id);

     Optional<Account> findByUserId(UUID userId);
}
