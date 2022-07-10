package com.onthe7.petking.module.user.infrastructure;

import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.module.user.domain.entity.AuthPrivateEntity;
import com.onthe7.petking.module.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthPrivateRepository extends JpaRepository<AuthPrivateEntity, Long> {

    Optional<AuthPrivateEntity> findByEmail(String email);

    Optional<AuthPrivateEntity> findByEmailAndProvider(String email, AuthProviderType provider);

    Optional<AuthPrivateEntity> findByUserAndProvider(UserEntity user, AuthProviderType provider);
}
