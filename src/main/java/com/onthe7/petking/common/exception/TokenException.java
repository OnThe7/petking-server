package com.onthe7.petking.common.exception;

import com.onthe7.petking.common.enums.ErrorCode;

public class TokenException {

    public static class TokenInvalidException extends BusinessException {
        public TokenInvalidException() {
            super(ErrorCode.COMMON_JWT_TOKEN_INVALID, "");
        }
    }

    public static class TokenExpiredException extends BusinessException {

        public TokenExpiredException() {
            super(ErrorCode.COMMON_JWT_TOKEN_EXPIRED, "");
        }

    }
}
