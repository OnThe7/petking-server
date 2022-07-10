package com.onthe7.petking.module.user.domain.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Jwt Payload 값으로 설정 가능
 * UsernamePasswordAuthenticationToken 객체로 해당 DTO를 가져올 수 있음
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserTokenDto implements UserDetails {

    private String userId; // sub
    private String email; // email
    private List<String> roleCode; // roles

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserTokenDto user = (UserTokenDto) object;
        return Objects.equals(userId, user.userId);
    }
}
