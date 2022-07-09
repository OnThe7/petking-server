package com.onthe7.petking.common.vo;

import com.onthe7.petking.common.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse
                .builder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().name())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    public static Response toResponse(ErrorCode errorCode) {
        return Response.error(ErrorResponse.from(errorCode));
    }
}
