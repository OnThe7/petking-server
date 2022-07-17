package com.onthe7.petking.module.user.application;

import com.onthe7.petking.common.exception.UserException;
import com.onthe7.petking.module.user.domain.dto.AuthDto.UserSignUpDto;
import com.onthe7.petking.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import com.onthe7.petking.module.user.infrastructure.AuthPrivateRepository;
import com.onthe7.petking.module.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final AuthPrivateRepository authPrivateRepository;

    @Transactional(rollbackOn = Exception.class)
    public String createUser(UserSignUpDto userSignUpDto) {
        authPrivateRepository.findByPrincipalIdAndProvider(userSignUpDto.getPrincipalId(), userSignUpDto.getProvider())
                .ifPresent(param -> {throw new UserException.UserAlreadySignedUpException();});
        UserEntity user = UserEntity.createUser(userSignUpDto);
        AuthPrivateEntity authPrivate = AuthPrivateEntity.createOAuthPrivateEntity(userSignUpDto, user);
        userRepository.save(user);
        authPrivateRepository.save(authPrivate);
        return authPrivate.getPrincipalId();
    }
}
