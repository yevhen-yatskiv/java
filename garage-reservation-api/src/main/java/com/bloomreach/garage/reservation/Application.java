package com.bloomreach.garage.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public final class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
