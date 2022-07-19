package com.onthe7.petking.module.user.application;

import com.onthe7.petking.common.security.service.SecurityUserService;
import com.onthe7.petking.module.user.domain.vo.AuthVo;
import com.onthe7.petking.module.user.domain.vo.AuthVo.ReissueTokenVo;
import com.onthe7.petking.module.user.domain.vo.AuthVo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.onthe7.petking.module.user.domain.dto.AuthDto.*;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final SecurityUserService securityUserService;
    private final UserCommandService userCommandService;

    public UserLoginVo signup(UserSignUpDto userSignUpDto) {
        String principalId = userCommandService.createUser(userSignUpDto);
        UserCredentialDto userCredential = securityUserService.getUserCredentialByPrincipalId(principalId);
        return setLoginByUserDetail(userCredential);
    }

    public UserLoginVo login(UserLoginDto userLoginDto) {
        UserCredentialDto userCredential = securityUserService
                .getUserCredentialByPrincipalId(userLoginDto.getPrincipalId());
        return setLoginByUserDetail(userCredential);
    }

    private UserLoginVo setLoginByUserDetail(UserCredentialDto userCredential) {
        securityUserService.setUserAuthentication(userCredential);

        String accessToken = securityUserService.getToken(userCredential.getPrincipalId());
        String refreshToken = securityUserService.getRefreshToken(userCredential.getPrincipalId());

        return new UserLoginVo(accessToken, refreshToken, userCredential.getClientId());
    }

    public ReissueTokenVo reissueToken(ReissueTokenDto reissueTokenDto) {
        return securityUserService.reissue(reissueTokenDto);
    }
}
