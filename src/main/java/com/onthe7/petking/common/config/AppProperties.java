package com.onthe7.petking.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    /* Redis */
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.ssl}")
    private boolean useSSl;

    /* JWT */
    @Value("${app.auth.tokenIssuer}")
    private String tokenIssuer;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpiry}")
    private int tokenExpiry;

    @Value("${app.auth.refreshTokenExpiry}")
    private int refreshTokenExpiry;
}
