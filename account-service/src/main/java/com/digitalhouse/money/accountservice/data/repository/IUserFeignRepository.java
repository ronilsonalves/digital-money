package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "users-service")
public interface IUserFeignRepository {
    @GetMapping("/users/{userUUID}")
    UserResponse getUserByUUID(@PathVariable UUID userUUID);
}