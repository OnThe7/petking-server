package com.onthe7.petking.common.handler;

//import com.onthe7.petking.common.enums.ErrorCode;
import com.onthe7.petking.common.exception.BusinessException;
import com.onthe7.petking.common.vo.ErrorResponse;
import com.onthe7.petking.common.vo.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected Response processError(BusinessException ex) {
        ErrorResponse errorResponse = ErrorResponse.from(ex.getErrorCode());
        return Response.error(errorResponse);
    }

//    @ExceptionHandler(value = {RuntimeException.class})
//    protected Response processError() {
//        ErrorResponse errorResponse = ErrorResponse.from(ErrorCode.COMMON_SYSTEM_ERROR);
//        return Response.error(errorResponse);
//    }
}
