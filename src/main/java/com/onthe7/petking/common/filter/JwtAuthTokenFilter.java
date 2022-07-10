package com.onthe7.petking.common.filter;

import com.onthe7.petking.common.exception.JwtException;
import com.onthe7.petking.common.util.JwtUtil;
import com.onthe7.petking.module.user.domain.dto.UserTokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // 다음과 같은 uri는 토큰검증x
        String[] equalsWith = {
                "/", "/webjars", "/health/ping",
        };

        // 다음으로 시작하는 uri는 토큰검증x
        String[] startWith = {
                "/login",
                "/error"
        };

        // 다음으로 끝나는 형식은 토큰검증x
        String[] endWith = {
                ".html", ".jpg", ".png", ".gif", ".ico", ".js", ".css"
        };

        boolean equalsWithPass = Arrays.asList(equalsWith).contains(uri);
        boolean startWithPass = Arrays.stream(startWith).anyMatch(uri::startsWith);
        boolean endWithPass = Arrays.stream(endWith).anyMatch(uri::endsWith);

        if (equalsWithPass || startWithPass || endWithPass) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("doFilterInternal");
        log.debug("[url]" + uri);

        try {
            String token = getToken(request);

            if (token != null && jwtUtil.validateJwtToken(token)) {

                List<GrantedAuthority> authorities = new ArrayList<>();
                List<String> roles = (List<String>) jwtUtil.getBodyValue(token, "roles");

                for (String roleCode : roles) {
                    authorities.add(new SimpleGrantedAuthority(roleCode));
                }

                UserTokenDto userInfo = UserTokenDto.builder()
                        .userId(jwtUtil.getBodyValue(token, "sub").toString())
                        .authorities(authorities)
                        .build();

                log.debug("[userInfo]" + userInfo.toString());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                request.setAttribute("Authentication", token);
                response.setHeader("Authentication", token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        log.info("JWT Token Authenticated");
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            throw new JwtException.JwtNotFountException();
        }

        if (authorization.startsWith("Bearer ")) {
            return authorization.replace("Bearer ", "");
        }

        return authorization;
    }

}
