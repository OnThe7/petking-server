package com.onthe7.petking.common.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onthe7.petking.common.config.AppProperties;
import com.onthe7.petking.common.exception.TokenException.TokenExpiredException;
import com.onthe7.petking.common.exception.TokenException.TokenInvalidException;
import com.onthe7.petking.common.exception.UserException;
import com.onthe7.petking.common.security.domain.JwtUserDetails;
import com.onthe7.petking.module.user.domain.dto.AuthDto;
import com.onthe7.petking.module.user.domain.dto.AuthDto.UserCredentialDto;
import com.onthe7.petking.module.user.domain.entity.UserRefreshToken;
import com.onthe7.petking.module.user.domain.vo.AuthVo.ReissueTokenVo;
import com.onthe7.petking.module.user.infrastructure.UserRefreshTokenRepository;
import com.onthe7.petking.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUserService.class);

    private final AppProperties appProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;

    public UserCredentialDto getUserCredentialByPrincipalId(String principalId) {
        return userRepository.getUserCredential(principalId)
                .orElseThrow(UserException.UserNotFoundException::new);
    }

    public void setUserAuthentication(UserCredentialDto userCredential) {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        userCredential.getPrincipalId(), null, List.of(userCredential.getRole())));
    }

    public String getToken(String principalId) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofMillis(appProperties.getTokenExpiry()));
        return JWT.create().withIssuer(appProperties.getTokenIssuer()).withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiry)).withSubject(principalId).sign(algorithm);
    }

    public ReissueTokenVo reissue(AuthDto.ReissueTokenDto input) {
        String principalId = JWT.decode(input.getAccessToken()).getSubject();
        UserCredentialDto userCredential = getUserCredentialByPrincipalId(principalId);

        UserRefreshToken redisToken =
                userRefreshTokenRepository.findById(userCredential.getPrincipalId())
                        .orElseThrow(NoSuchElementException::new);

        if (redisToken.getRefreshToken().equals(input.getRefreshToken())) {
            return reissueRefreshToken(redisToken.getRefreshToken(), userCredential);
        }

        throw new IllegalArgumentException();
    }

    private ReissueTokenVo reissueRefreshToken(String refreshToken, UserCredentialDto userCredential) {
        DecodedJWT decodedJwt = getDecodedToken(refreshToken).orElseThrow(TokenExpiredException::new);

        if (decodedJwt.getExpiresAt().before(new Date())) {
            String accessToken = getToken(userCredential.getPrincipalId());
            return new ReissueTokenVo(accessToken, getRefreshToken(userCredential.getPrincipalId()));
        }
        return new ReissueTokenVo(getToken(userCredential.getPrincipalId()), refreshToken);
    }

    public String getRefreshToken(String principalId) {
        Instant now = Instant.now();
        Instant expiry = Instant.now().plus(Duration.ofMillis(appProperties.getRefreshTokenExpiry()));
        String token = JWT.create().withIssuer(appProperties.getTokenIssuer())
                .withIssuedAt(Date.from(now)).withExpiresAt(Date.from(expiry))
                .withSubject(appProperties.getTokenSecret()).sign(algorithm);

        saveUserRefreshToken(principalId, token);

        return token;
    }

    private void saveUserRefreshToken(String principalId, String token) {
        UserRefreshToken refreshToken = UserRefreshToken.builder().principalId(principalId)
                .refreshToken(token).build();

        userRefreshTokenRepository.deleteById(refreshToken.getPrincipalId());
        userRefreshTokenRepository.save(refreshToken);
    }

    public JwtUserDetails loadUserByToken(String token) {
        return getDecodedToken(token)
                .map(DecodedJWT::getSubject)
                .map(this::getUserCredentialByPrincipalId)
                .map(userCredentialDto -> this.getUserDetails(userCredentialDto, token))
                .orElse(null);
    }

    private JwtUserDetails getUserDetails(UserCredentialDto userCredentialDto, String accessToken) {

        return JwtUserDetails.builder().username(userCredentialDto.getPrincipalId())
                .password(userCredentialDto.getPrincipalId())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(userCredentialDto.getRole().name())))
                .token(accessToken).build();
    }

    private Optional<DecodedJWT> getDecodedToken(String token) {
        try {
            Optional<DecodedJWT> decodedJWT = Optional.of(verifier.verify(token));
            logger.debug("getDecodedToken() -> decoded subject : {}", decodedJWT.get().getSubject());
            return decodedJWT;
        } catch(JWTDecodeException ex) {
            throw new TokenInvalidException();
        } catch(JWTVerificationException ex) {
            throw new TokenExpiredException();
        }
    }

    public String getPrincipalIdBySecurityContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
