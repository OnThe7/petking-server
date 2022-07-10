package com.onthe7.petking.common.security.service;

import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.common.enums.YesNo;
import com.onthe7.petking.common.exception.UserException.UserNotFoundException;
import com.onthe7.petking.common.exception.UserException.UserNotLoggedInException;
import com.onthe7.petking.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import com.onthe7.petking.module.user.infrastructure.AuthPrivateRepository;
import com.onthe7.petking.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

import static com.onthe7.petking.common.enums.AuthProviderType.OAUTH_GOOGLE;
import static com.onthe7.petking.common.enums.UserRole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private final AuthPrivateRepository authPrivateRepository;

    private final UserRepository userRepository;

    public UserEntity loadUser(String email, AuthProviderType authProviderType) {
        AuthPrivateEntity authPrivate = authPrivateRepository.findByEmailAndProvider(email, OAUTH_GOOGLE)
                .orElse(null);
        return Objects.nonNull(authPrivate) ?
                userRepository.findById(authPrivate.getUserId())
                        .orElseThrow(UserNotFoundException::new):
                saveUser(email, authProviderType);
    }


    private UserEntity saveUser(String email, AuthProviderType authProviderType) {
        UserEntity user = UserEntity.builder()
                .clientId(RandomStringUtils.randomAlphanumeric(30))
                .nickname(generateNicknameByEmail(email)).role(ROLE_USER)
                .build();
        userRepository.save(user);
        AuthPrivateEntity authPrivate = AuthPrivateEntity.builder()
                .user(user).userId(user.getId())
                .email(email).provider(authProviderType).emailVerified(YesNo.Y).build();
        authPrivateRepository.save(authPrivate);
        return user;
    }

    private String generateNicknameByEmail(String email) {
        return email.split("@")[0] + String.format("_%04d", new Random().nextInt(10000));
    }

    public String getClientIdBySecurityContext() {
        String clientId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (clientId.equals("anonymousUser")) throw new UserNotLoggedInException();
        return clientId;
    }

    public UserEntity getLoginUser(String clientId) {
        return userRepository.findByClientId(clientId)
                .orElseThrow(UserNotFoundException::new);
    }
}
