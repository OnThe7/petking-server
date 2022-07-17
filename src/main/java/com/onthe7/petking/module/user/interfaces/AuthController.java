package com.onthe7.petking.module.user.interfaces;

import com.onthe7.petking.common.security.service.SecurityUserService;
import com.onthe7.petking.common.util.RequestIdGenerator;
import com.onthe7.petking.common.vo.Response;
import com.onthe7.petking.module.user.application.AuthFacade;
import com.onthe7.petking.module.user.domain.dto.AuthDto;
import com.onthe7.petking.module.user.domain.dto.AuthDto.ReissueTokenDto;
import com.onthe7.petking.module.user.domain.dto.AuthDto.UserLoginDto;
import com.onthe7.petking.module.user.domain.dto.AuthDto.UserSignUpDto;
import com.onthe7.petking.module.user.domain.vo.AuthVo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final RequestIdGenerator requestIdGenerator;
    private final AuthFacade authFacade;

    @PostMapping("/signup")
    public Response signUp(@RequestBody UserSignUpDto userSignUpDto) {
        String requestId = requestIdGenerator.getRequestId();
        UserLoginVo loginVo = authFacade.signup(userSignUpDto);

        return Response.success(requestId, loginVo);
    }

    @PostMapping("/login")
    public Response login(@RequestBody UserLoginDto userLoginDto) {
        String requestId = requestIdGenerator.getRequestId();
        UserLoginVo loginVo = authFacade.login(userLoginDto);
        return Response.success(requestId, loginVo);
    }

    @PostMapping("/token/reissue")
    public Response reissueToken(@RequestBody ReissueTokenDto reissueTokenDto) {
        String requestId = requestIdGenerator.getRequestId();
        return Response.success(requestId, authFacade.reissueToken(reissueTokenDto));
    }
}
