package com.digitalhouse.money.usersservice.data.repository;

import com.digitalhouse.money.usersservice.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String userEmailAddress);

    Optional<User> findUserByEmailVerificationCode(String emailVerificationCode);

    boolean existsById(UUID userID);
}