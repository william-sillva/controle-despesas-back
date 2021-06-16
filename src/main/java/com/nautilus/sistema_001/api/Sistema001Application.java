package com.nautilus.sistema_001.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * FIXME - Removendo Security SpringBoot
 * ex: @SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
 */

@SpringBootApplication
public class Sistema001Application {

    public static void main(String[] args) {
        SpringApplication.run(Sistema001Application.class, args);
    }

}
