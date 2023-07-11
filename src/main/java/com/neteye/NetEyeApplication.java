package com.neteye;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class NetEyeApplication implements ApplicationRunner {
    public static final Logger logger = LogManager.getLogger(NetEyeApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(NetEyeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
