package com.neteye.ControllerTests;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.DriverTimeoutException;
import com.neteye.config.SecurityConfig;
import com.neteye.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.InetSocketAddress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @MockBean
    UserService userService;

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

    //@BeforeEach
    void mockMvcSetUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }



    @AfterAll
    static void flushDB() {
        try {
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
        assertThat(context).isNotNull();
    }

    @Test
    void shouldGetCsrfToken() throws Exception {
        mockMvc.perform(get("/csrf")).andExpect(status().isOk());
    }


}
