package com.onthe7.petking.module.user.domain.entity;

import com.onthe7.petking.common.domain.entity.BaseEntity;
import com.onthe7.petking.common.enums.AuthProviderType;
import com.onthe7.petking.common.enums.YesNo;
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

    @Column(name = "user_id")
    private Long userId;

    private String email;

    private String password;

    @Enumerated(STRING)
    private AuthProviderType provider;

    @Enumerated(STRING)
    private YesNo emailVerified;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
}
