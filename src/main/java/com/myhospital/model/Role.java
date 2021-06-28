package com.myhospital.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,ROLE_DOCTOR,ROLE_NURSE;

    @Override
    public String getAuthority() {
        return name();
    }
}
