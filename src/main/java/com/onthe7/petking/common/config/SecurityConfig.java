package com.onthe7.petking.common.config;

import com.onthe7.petking.common.aspects.CustomEntryPoint;
import com.onthe7.petking.common.filter.OauthFilter;
import com.onthe7.petking.common.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomEntryPoint customEntryPoint;
    private final CustomOAuth2UserService oAuth2UserService;
    private final OauthFilter oauthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
            .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/health/ping").permitAll() // health check
            .and()
                .exceptionHandling()
//                .authenticationEntryPoint(customEntryPoint)
                .accessDeniedHandler(customEntryPoint)
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService);
        http
                .addFilterAfter(oauthFilter, OAuth2LoginAuthenticationFilter.class);

    }
}
