package com.jobportal.onlinejobportal.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String token;

    public JwtAuthenticationToken(String email, String token) {
        super(null);
        this.email = email;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}
