package com.neteye.integrationTest;

import com.neteye.TestContainers;
import com.neteye.controllers.UserController;
import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.repositories.UserRepository;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mock;

@AutoConfigureMockMvc
class UsersTest extends TestContainers {

    @Autowired
    UserController userController;
    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void clearDb() {
        userRepository.deleteAll();
    }

    @Test
    void shouldAddNewUser() {
        UserDto userDto = new UserDto("name", "name", "password", "password", "mail@mail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        userController.registerAccount(userDto, request);
        Assertions.assertAll(
                () -> Assertions.assertTrue(userRepository.findByEmail(userDto.getEmail()).isPresent()),
                () -> Assertions.assertEquals(userDto.getEmail(), userRepository.findByEmail(userDto.getEmail()).get().getEmail())
        );
    }

    @Test
    void shouldThrowUserAlreadyExistsExceptionWhenAddingUserWithEmailThatIsAlreadyInDatabase() {
        UserDto userDto = new UserDto("name", "name", "password", "password", "mail@mail.com");
        UserDto userDto2 = new UserDto("name2", "name2", "password2", "password2", "mail@mail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletRequest request2 = mock(HttpServletRequest.class);
        userController.registerAccount(userDto, request);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userController.registerAccount(userDto2, request2));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenTryingToLoginWithAccountThatIsNotInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType("application/json")
                        .content("{\"email\": \"not@in.db\", \"password\": \"password\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UserDto userDto = new UserDto("name", "name", "password", "password", "mail@mail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        userController.registerAccount(userDto, request);
        Assertions.assertTrue(userRepository.findByEmail(userDto.getEmail()).isPresent());

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType("application/json")
                        .content("{\"email\": \"mail@mail.com\", \"password\": \"password\"}")
                        .session(session)
                );
        mockMvc.perform(MockMvcRequestBuilders.post("/account/delete").session(session));
        Assertions.assertTrue(userRepository.findByEmail(userDto.getEmail()).isEmpty());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserDto userDto = new UserDto("name", "name", "password", "password", "mail@mail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        userController.registerAccount(userDto, request);
        Assertions.assertTrue(userRepository.findByEmail(userDto.getEmail()).isPresent());

        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType("application/json")
                .content("{\"email\": \"mail@mail.com\", \"password\": \"password\"}")
                .session(session)
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/account/update")
                .contentType("application/json")
                .content("{\"firstName\": \"notName\", \"lastName\": \"notName\", \"password\":  \"\", \"passwordConfirmation\": \"\"}")
                .session(session));
        Assertions.assertAll(
                () -> Assertions.assertEquals("notName", userRepository.findByEmail(userDto.getEmail()).get().getFirstName()),
                () -> Assertions.assertEquals("notName", userRepository.findByEmail(userDto.getEmail()).get().getLastName())
        );
    }


}
