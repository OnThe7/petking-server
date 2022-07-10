package com.onthe7.petking.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다"),
    USER_EMAIL_NOT_VERIFIED(HttpStatus.NOT_FOUND, "이메일 인증이 필요합니다"),
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생했습니다"),
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... arg) {
        return String.format(message, arg);
    }
}
