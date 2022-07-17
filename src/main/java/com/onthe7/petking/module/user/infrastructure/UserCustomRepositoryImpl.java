package com.onthe7.petking.module.user.infrastructure;

import com.onthe7.petking.module.user.domain.dto.AuthDto.UserCredentialDto;
import com.onthe7.petking.module.user.domain.dto.QAuthDto_UserCredentialDto;
import com.onthe7.petking.module.user.domain.entity.QAuthPrivateEntity;
import com.onthe7.petking.module.user.domain.entity.QUserEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;
import java.util.Optional;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final QUserEntity user = QUserEntity.userEntity;
    private final QAuthPrivateEntity authPrivate = QAuthPrivateEntity.authPrivateEntity;

    @Override
    public Optional<UserCredentialDto> getUserCredential(String principalId) {
        validateUserCredentialQueryParams(principalId);

        return Optional.ofNullable(queryFactory
                .select(getUserCredentialProjection())
                .from(user)
                .leftJoin(authPrivate).on(authPrivate.user.eq(user))
                .where(authPrivate.principalId.eq(principalId))
                .fetchOne());
    }

    private void validateUserCredentialQueryParams(String principalId) {
        if (principalId.isEmpty()) throw new InvalidParameterException();
    }

    private Expression<UserCredentialDto> getUserCredentialProjection() {
        return new QAuthDto_UserCredentialDto(user.clientId, user.role, authPrivate.principalId);
    }
}
