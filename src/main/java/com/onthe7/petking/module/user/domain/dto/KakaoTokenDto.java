package com.onthe7.petking.module.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoTokenDto {
    private String tokenType;

    private String accessToken;

    private Long expiresIn;

    private String idToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String scope;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long refreshTokenExpiresIn;
}
