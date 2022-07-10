package com.onthe7.petking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PetkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetkingApplication.class, args);
    }

}
