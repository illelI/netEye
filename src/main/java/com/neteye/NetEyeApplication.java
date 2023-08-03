package com.neteye;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableCassandraRepositories
public class NetEyeApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(NetEyeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception{

    }
}
