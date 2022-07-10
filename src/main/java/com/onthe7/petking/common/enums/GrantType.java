package com.onthe7.petking.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GrantType {
    AUTH_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token");

    private String value;
}
