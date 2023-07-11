package com.neteye.persistenceTests;

import static org.assertj.core.api.Assertions.assertThat;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.neteye.persistence.entities.Device;
import static org.junit.Assert.*;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class DeviceEntityTest {
    @Container
    public static final CassandraContainer cassandra
            = (CassandraContainer) new CassandraContainer("cassandra").withExposedPorts(9042);

    private static final String KEYSPACE_NAME = "netEye";
    @Autowired
    TestEntityManager entityManager;

    private Device device;

    @Before
    public void setUp() {
        device = new Device("10.10.10.10", 22, "sample info");
    }
    @BeforeAll
    static void setupCassandraConnectionProperties() {
        System.setProperty("spring.data.cassandra.keyspace-name", "netEye");
        System.setProperty("spring.data.cassandra.contact-points", cassandra.getContainerIpAddress());
        System.setProperty("spring.data.cassandra.port", String.valueOf(cassandra.getMappedPort(9042)));

        createKeyspace(cassandra.getCluster());
    }

    static void createKeyspace(Cluster cluster) {
        try(Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME + " WITH replication = \n" +
                    "{'class':'SimpleStrategy','replication_factor':'1'};");
        }
    }


    @Test
    public void shouldCassandraContainerRunning() {
        assertThat(cassandra.isRunning()).isTrue();
    }

    @Test
    public void shouldSaveDevice() {
        Device savedDevice = entityManager.persistAndFlush(device);
        assertEquals(savedDevice.getIp(), device.getIp());
    }
}
