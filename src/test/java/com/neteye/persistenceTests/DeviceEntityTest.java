package com.neteye.persistenceTests;

import static org.assertj.core.api.Assertions.assertThat;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.Port;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeviceEntityTest {

    @Autowired
    TestEntityManager entityManager;

    private Device device;
    private Port port;

    @Before
    public void setUp() {
        device = new Device("10.10.10.10", new ArrayList<>());
        port = new Port(1L, 25, "Test");
        device.addOpenPort(port);
    }

    @Test
    public void saveDevice() {
        Device savedDevice = this.entityManager.persistAndFlush(device);
        assertThat(savedDevice.getIp()).isEqualTo("10.10.10.10");
    }
}
