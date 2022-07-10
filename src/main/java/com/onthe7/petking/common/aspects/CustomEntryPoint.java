package com.onthe7.petking.common.aspects;

import com.onthe7.petking.common.enums.ErrorCode;
import com.onthe7.petking.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 401 unauthorized forward
        log.error("Unauthorized error. Message - {}", e.getMessage());
        handlerExceptionResolver.resolveException(request, response, null, new BusinessException(ErrorCode.AUTH_FAILED));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        // 403 forbidden forward
        log.error("Access Denied error. Message - {}", e.getMessage());
        handlerExceptionResolver.resolveException(request, response, null, new BusinessException(ErrorCode.NO_PERMISSION));
    }
}