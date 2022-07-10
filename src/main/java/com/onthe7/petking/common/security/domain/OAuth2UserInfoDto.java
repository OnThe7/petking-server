package com.onthe7.petking.common.security.domain;

import lombok.Getter;

import java.util.Random;

@Getter
public class OAuth2UserInfoDto {
    private final String principalId;
    private final String nickname;
    private final String email;

    public OAuth2UserInfoDto(String principalId, String nickname, String email) {
        this.principalId = principalId;
        this.nickname = generateNicknameWithRandomNumber(nickname);
        this.email = email;
    }

    private String generateNicknameWithRandomNumber(String name) {
        return name + String.format("_%04d", new Random().nextInt(10000));
    }
}
