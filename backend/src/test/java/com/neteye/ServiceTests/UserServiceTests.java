package com.neteye.ServiceTests;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.DriverTimeoutException;
import com.neteye.persistence.dto.AccountUpdateDto;
import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.entities.User;
import com.neteye.persistence.repositories.UserRepository;
import com.neteye.services.UserService;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.net.InetSocketAddress;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTests {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeAll
    static void setup() {
        CqlSessionBuilder builder = CqlSession.builder();
        try (CqlSession session = builder.addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1").build()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS test WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};");
        }
        System.setProperty("spring.cassandra.keyspace-name", "test");
        System.setProperty("spring.cassandra.contact-points", "127.0.0.1");
        System.setProperty("spring.cassandra.port", "9042");
        System.setProperty("spring.cassandra.local-datacenter", "datacenter1");
        System.setProperty("spring.cassandra.schema-action", "create_if_not_exists");
    }

    @AfterAll
    static void flushDB() {
        try{
        CqlSessionBuilder builder = CqlSession.builder();
        try (CqlSession session = builder.addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1").build()) {
            session.execute("DROP KEYSPACE test;");
        }
        } catch (DriverTimeoutException ex) {
            //
        }
    }

    @Test
    void contextLoad() {
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
    }


    @Test
    void shouldCreateNewUser() {
        UserDto userDto = new UserDto("name", "name", "password", "password", "mail@mail.com");
        User user = userService.createUser(userDto);
        assertThat(user.getId()).isEqualTo(userRepository.findByEmail("mail@mail.com").get().getId());
    }

    @Test
    void shouldThrowUserAlreadyExistExceptionWhenAddingUserWithEmailThatIsAlreadyInDatabase() {
        UserDto userDto1 = new UserDto("name1", "name1", "password1", "password1", "mail1@mail.com");
        UserDto userDto2 = new UserDto("name2", "name2", "password2", "password2", "mail1@mail.com");
        userService.createUser(userDto1);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto2));
    }

    @Test
    void shouldLoadUserByItsEmail() {
        UserDto userDto = new UserDto("name3", "name3", "password3", "password3", "mail3@mail.com");
        User user = userService.createUser(userDto);
        User user1 = (User) userService.loadUserByUsername(user.getEmail());
        assertThat(user.getId()).isEqualTo(user1.getId());
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenTryingToLoadUserWithEmailThatIsNotInDatabase() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("not@in.db"));
    }

    @Test
    void shouldUpdateUserInfo() {
        UserDto userDto = new UserDto("name11", "name11", "password", "password", "mail11@mail.com");
        User existingUser = userService.createUser(userDto);
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setFirstName("name12");
        accountUpdateDto.setLastName("");
        accountUpdateDto.setPassword("");
        accountUpdateDto.setPasswordConfirmation("");
        User user = userService.updateUser(accountUpdateDto, existingUser);
        assertThat(user.getFirstName()).isEqualTo(accountUpdateDto.getFirstName());
    }

}
