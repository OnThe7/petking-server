package com.onthe7.petking.common.enums;

import com.onthe7.petking.common.exception.BusinessException;
import lombok.Getter;

@Getter
public enum AuthProviderType {
    PASSWORD("PASSWORD", "password"),
    OAUTH_GOOGLE("OAUTH_GOOGLE", "google"),
    OAUTH_KAKAO("OAUTH_KAKAO", "kakao");

    private final String name;
    private final String registrationId;

    public static AuthProviderType from(String registrationId) {
        if (registrationId.equals(PASSWORD.getRegistrationId())){
            return PASSWORD;
        } else if (registrationId.equals(OAUTH_GOOGLE.registrationId)) {
            return OAUTH_GOOGLE;
        } else if (registrationId.equals(OAUTH_KAKAO.registrationId)) {
            return OAUTH_KAKAO;
        } else {
            throw new BusinessException("지원 되지 않는 로그인 방식입니다");
        }
    }

    private AuthProviderType(String name, String registrationId) {
        this.name = name;
        this.registrationId = registrationId;
    }
}
