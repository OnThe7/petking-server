package com.onthe7.petking.common.security.service;

import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.common.enums.YesNo;
import com.onthe7.petking.common.exception.BusinessException;
import com.onthe7.petking.common.exception.UserException.UserNotFoundException;
import com.onthe7.petking.common.exception.UserException.UserNotLoggedInException;
import com.onthe7.petking.common.security.domain.OAuth2UserInfoDto;
import com.onthe7.petking.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import com.onthe7.petking.module.user.infrastructure.AuthPrivateRepository;
import com.onthe7.petking.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

import static com.onthe7.petking.common.enums.UserRole.ROLE_USER;

@Service
@RequiredArgsConstructor
public class SecurityUserService {

    private final AuthPrivateRepository authPrivateRepository;

    private final UserRepository userRepository;

    public UserEntity loadUser(OAuth2AuthenticationToken authToken) {
        AuthProviderType authProviderType = AuthProviderType.from(authToken.getAuthorizedClientRegistrationId());
        OAuth2UserInfoDto userInfoDto = getUserInfoDto(authToken, authProviderType);
        AuthPrivateEntity authPrivate = authPrivateRepository.findByPrincipalIdAndProvider(userInfoDto.getPrincipalId(), authProviderType)
                .orElse(null);
        return Objects.nonNull(authPrivate) ?
                userRepository.findById(authPrivate.getUserId())
                        .orElseThrow(UserNotFoundException::new):
                saveUser(userInfoDto, authProviderType);
    }

    private OAuth2UserInfoDto getUserInfoDto(OAuth2AuthenticationToken authToken, AuthProviderType providerType) {
        switch (providerType) {
            case OAUTH_GOOGLE:
                Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
                String givenName = authToken.getPrincipal().getAttribute("given_name");
                String email = authToken.getPrincipal().getAttribute("email");
                String principalId = attributes.get("sub").toString();
                return new OAuth2UserInfoDto(principalId, givenName, email);
            case OAUTH_KAKAO:
                Map<String, Object> properties = authToken.getPrincipal().getAttribute("properties");
                Map<String, Object> kakaoAccount = authToken.getPrincipal().getAttribute("kakao_account");
                return new OAuth2UserInfoDto(
                        Objects.requireNonNull(authToken.getPrincipal().getAttribute("id")).toString(),
                        Objects.requireNonNull(properties).get("nickname").toString(),
                        Objects.requireNonNull(kakaoAccount).get("email").toString());
            default:
                // todo : exception 변경
                throw new BusinessException("Unsupported Provider Type");
        }
    }


    private UserEntity saveUser(OAuth2UserInfoDto userInfoDto, AuthProviderType authProviderType) {
        UserEntity user = UserEntity.builder()
                .clientId(RandomStringUtils.randomAlphanumeric(30))
                .nickname(userInfoDto.getNickname()).role(ROLE_USER)
                .build();
        userRepository.save(user);
        Long userId = user.getId();
        AuthPrivateEntity authPrivate = AuthPrivateEntity.builder()
                .user(user).userId(userId)
                .email(userInfoDto.getEmail())
                .principalId(userInfoDto.getPrincipalId())
                .provider(authProviderType).emailVerified(YesNo.Y)
                .createdBy(userId).updatedBy(userId)
                .build();
        authPrivateRepository.save(authPrivate);
        return user;
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
