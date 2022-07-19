package com.onthe7.petking.module.user.infrastructure;

import com.onthe7.petking.module.user.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, UserCustomRepository {
    Optional<UserEntity> findByClientId(String clientId);

    Optional<UserEntity> findById(Long id);
}