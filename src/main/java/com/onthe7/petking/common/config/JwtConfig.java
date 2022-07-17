package com.onthe7.petking.common.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final AppProperties appProperties;

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(appProperties.getTokenSecret());
    }

    @Bean
    public JWTVerifier verifier(Algorithm algorithm) {
        return JWT
                .require(algorithm)
                .withIssuer(appProperties.getTokenIssuer())
                .build();
    }
}
