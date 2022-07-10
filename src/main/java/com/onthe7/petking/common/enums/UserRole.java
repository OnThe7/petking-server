package com.onthe7.petking.common.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN");

    private String authority;

    private UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
