package com.onthe7.petking.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    CLIENT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // 401, 403
    AUTH_FAILED(HttpStatus.UNAUTHORIZED, "잘못된 접근입니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 500
    COMMON_SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... arg) {
        return String.format(message, arg);
    }
}
