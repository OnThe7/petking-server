package com.onthe7.petking.module.user.interfaces;

import com.onthe7.petking.common.security.service.SecurityUserService;
import com.onthe7.petking.common.vo.Response;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final SecurityUserService securityUserService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login")
    public Response login() {
        return Response.success();
    }


    @GetMapping("/user")
    public Response getUser() {
        String clientId = securityUserService.getClientIdBySecurityContext();
        UserEntity loginUser = securityUserService.getLoginUser(clientId);
        return Response.success("ss", loginUser);
    }
}
