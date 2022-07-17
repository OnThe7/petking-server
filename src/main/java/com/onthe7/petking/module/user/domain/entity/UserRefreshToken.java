package com.onthe7.petking.module.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "jwt", timeToLive = 604800)
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRefreshToken {

    @Id
    private String principalId;
    private String refreshToken;

}
