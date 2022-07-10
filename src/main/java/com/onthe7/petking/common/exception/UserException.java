package com.onthe7.petking.common.exception;

import com.onthe7.petking.common.enums.ErrorCode;

public class UserException {

    public static class UserNotFoundException extends BusinessException {
        public UserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }
    }

    public static class UserNotVerifiedException extends BusinessException {
        public UserNotVerifiedException() {
            super(ErrorCode.USER_EMAIL_NOT_VERIFIED);
        }
    }

    public static class UserNotLoggedInException extends BusinessException {
        public UserNotLoggedInException() {
            super(ErrorCode.USER_NOT_LOGGED_IN);
        }
    }

}
