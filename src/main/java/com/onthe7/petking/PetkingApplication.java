package com.onthe7.petking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EnableJpaAuditing
@ComponentScan("com.onthe7")
@EntityScan("com.onthe7")
@EnableJpaRepositories("com.onthe7")
@ServletComponentScan("com.onthe7")
@SpringBootApplication
public class PetkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetkingApplication.class, args);
    }

}
