package com.onthe7.petking.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 사용자 오류
    USER_EMAIL_NOT_VERIFIED(HttpStatus.NOT_FOUND, "이메일 인증이 필요합니다"),
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    CLIENT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401, 403
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // JWT 토큰 오류
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을수 없음."),
    TOKEN_DATE_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰 유효기간 만료."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰."),
    TOKEN_CREATED_FAILED(HttpStatus.UNAUTHORIZED, "토큰 생성 Error, 다시 시도해 주세요."),

    // 500
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... arg) {
        return String.format(message, arg);
    }
}
