package com.onthe7.petking.common.util;

import com.onthe7.petking.common.exception.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private String secretKey;

    // TODO: redis에 open id 공개키 저장 및 signing key로 가져오는 로직 작성
//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString();
//    }

    public Object getBodyValue(String token, String field) {
        if (this.validateJwtToken(token)) {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(field);
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;

        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e.getMessage());
            throw new JwtException.JwtInvalidException();

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e.getMessage());
            throw new JwtException.JwtInvalidException();

        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e.getMessage());
            throw new JwtException.JwtInvalidException();

        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e.getMessage());
            throw new JwtException.JwtInvalidException();

        }

    }

}
