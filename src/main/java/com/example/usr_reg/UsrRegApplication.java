package com.example.usr_reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class UsrRegApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsrRegApplication.class, args);
    }
}
