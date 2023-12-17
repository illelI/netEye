package com.neteye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class NetEyeApplication  {
    public static void main(String[] args) {
        SpringApplication.run(NetEyeApplication.class, args);
    }


}
