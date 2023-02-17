package com.digitalhouse.money.usersservice.api.service.iplm;

import com.digitalhouse.money.usersservice.api.request.UserRequestBody;
import com.digitalhouse.money.usersservice.data.model.User;
import com.digitalhouse.money.usersservice.data.repository.KeycloakRepository;
import com.digitalhouse.money.usersservice.data.repository.UserRepository;
import com.digitalhouse.money.usersservice.exceptionhandler.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KeycloakRepository keycloakRepository;

    @Autowired
    private UserServiceImpl userService;

    User userToBeReturned;

    UserRequestBody userToBeSaved;
    UserRequestBody userWithInvalidFields;

    @BeforeEach
    void setUp() {
        userToBeSaved = new UserRequestBody();
        userToBeSaved.setName("John");
        userToBeSaved.setLastName("Doe");
        userToBeSaved.setEmail("johndoe@mail.com");
        userToBeSaved.setPhone("99912345678");
        userToBeSaved.setCpf("611.024.140-77");
        userToBeSaved.setPassword("12345678");
        userToBeReturned = new User();
        BeanUtils.copyProperties(userToBeSaved,userToBeReturned);
        userToBeReturned.setId(UUID.randomUUID());
        userWithInvalidFields = new UserRequestBody();
        userWithInvalidFields.setName("Joel");
        userWithInvalidFields.setCpf("123456");
        userWithInvalidFields.setEmail("asdfascom");
    }

    @Test
    @DisplayName("shouldSaveSuccessfullyANewUser")
    void save() throws Exception {
        doReturn(userToBeReturned).when(keycloakRepository).save(userToBeSaved);
        doReturn(userToBeReturned).when(userRepository).save(userToBeReturned);
        User result = userService.save(userToBeSaved);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userToBeReturned.toString(),result.toString());
    }

    @Test
    @DisplayName("shouldNotSaveANewUserWithInvalidFields")
    void saveUnsuccessfully() throws Exception {
        doThrow(BadRequestException.class).when(keycloakRepository).save(userWithInvalidFields);
        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            User user = userService.save(userWithInvalidFields);
        });
        Assertions.assertNotNull(exception);
    }
}