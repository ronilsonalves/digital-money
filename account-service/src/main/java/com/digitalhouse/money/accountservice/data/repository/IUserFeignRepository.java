package com.digitalhouse.money.accountservice.data.repository;

import com.digitalhouse.money.accountservice.data.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

//Subtituir o FeignClient pela linha abaixo se for buildar Dockerfile da aplicação,
// do contrário algumas rotas falham pois a rede interna do Docker não passa a porta correta e a conexão é recusada.
//@FeignClient(name = "users-service", url = "http://users-service:8081")
@FeignClient(name = "users-service")
public interface IUserFeignRepository {
    @GetMapping("/users/{userUUID}")
    UserResponse getUserByUUID(@PathVariable UUID userUUID);
}