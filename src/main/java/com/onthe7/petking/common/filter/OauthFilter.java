package com.onthe7.petking.common.filter;

import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.common.security.service.SecurityUserService;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;

@Component
@WebFilter(urlPatterns = "/api/v1/auth/login")
@RequiredArgsConstructor
public class OauthFilter implements Filter {

    private final SecurityUserService securityUserService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) auth;
            AuthProviderType authProviderType = AuthProviderType.from(authToken.getAuthorizedClientRegistrationId());
            String email = (String) authToken.getPrincipal().getAttributes().get("email");
            UserEntity user = securityUserService.loadUser(email, authProviderType);
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            user.getClientId(), null, List.of(user.getRole())));
        }
        chain.doFilter(request, response);
    }
}
