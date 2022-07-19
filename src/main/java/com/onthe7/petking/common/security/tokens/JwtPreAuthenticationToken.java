package com.onthe7.petking.common.security.tokens;

import com.onthe7.petking.common.security.domain.JwtUserDetails;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Getter
public class JwtPreAuthenticationToken extends PreAuthenticatedAuthenticationToken {

    @Builder
    public JwtPreAuthenticationToken(JwtUserDetails principal, WebAuthenticationDetails details) {
        super(principal, null, principal.getAuthorities());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
