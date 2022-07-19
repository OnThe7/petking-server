package com.onthe7.petking.module.user.domain.entity;

import com.onthe7.petking.common.domain.entity.BaseEntity;
import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.common.enums.YesNo;
import com.onthe7.petking.module.user.domain.dto.AuthDto.UserSignUpDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@Table(name = "tbl_auth_private")
@NoArgsConstructor(access = PROTECTED)
public class AuthPrivateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(STRING)
    private AuthProviderType provider;

    private String principalId;


    @Enumerated(STRING)
    private YesNo verified;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public static AuthPrivateEntity createOAuthPrivateEntity(UserSignUpDto userSignUpDto, UserEntity user) {
        return AuthPrivateEntity.builder()
                .email(userSignUpDto.getEmail()).principalId(userSignUpDto.getPrincipalId())
                .provider(userSignUpDto.getProvider()).verified(YesNo.Y)
                .user(user).build();
    }
}
