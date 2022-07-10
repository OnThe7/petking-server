package com.onthe7.petking.common.exception;

import com.onthe7.petking.common.enums.ErrorCode;

public class JwtException {

    public static class JwtNotFountException extends BusinessException {
        public JwtNotFountException() {
            super(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    public static class JwtExpiredException extends BusinessException {
        public JwtExpiredException() {
            super(ErrorCode.TOKEN_DATE_EXPIRED);
        }
    }

    public static class JwtInvalidException extends BusinessException {
        public JwtInvalidException() {
            super(ErrorCode.TOKEN_INVALID);
        }
    }

    public static class JwtCreatedFailedException extends BusinessException {
        public JwtCreatedFailedException() {
            super(ErrorCode.TOKEN_CREATED_FAILED);
        }
    }

}
