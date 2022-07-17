package com.onthe7.petking.module.user.infrastructure;

import com.onthe7.petking.module.user.domain.dto.AuthDto.UserCredentialDto;

import java.util.Optional;

public interface UserCustomRepository {
    Optional<UserCredentialDto> getUserCredential(String principalId);
}
