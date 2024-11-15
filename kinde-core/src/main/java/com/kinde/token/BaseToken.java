package com.kinde.token;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;

public abstract class BaseToken implements KindeToken {
    protected SignedJWT jwt;
    protected String rawToken;
    protected boolean valid;
    protected JWTClaimsSet claims;

    protected BaseToken(String token, boolean valid) {
        this.rawToken = token;
        this.valid = valid;
        
        if (token != null) {
            try {
                this.jwt = SignedJWT.parse(token);
                this.claims = this.jwt.getJWTClaimsSet();
            } catch (java.text.ParseException e) {
                // Token might be a simple string, not a JWT
                this.jwt = null;
                this.claims = null;
            }
        }
    }

    @Override
    public String token() {
        return rawToken;
    }

    @Override
    public boolean valid() {
        return valid;
    }

    @Override
    public String getUser() {
        return claims != null ? claims.getSubject() : null;
    }

    @Override
    public List<String> getOrganisations() {
        return Collections.emptyList();
    }

    @Override
    public Object getClaim(String key) {
        return claims != null ? claims.getClaim(key) : null;
    }

    @Override
    public List<String> getPermissions() {
        if (claims != null) {
            try {
                Object permissions = claims.getClaim("permissions");
                if (permissions instanceof List) {
                    return (List<String>) permissions;
                }
            } catch (Exception e) {
                // Failed to get permissions, return empty list
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String getStringFlag(String key) {
        Object value = getClaim(key);
        return value instanceof String ? (String) value : null;
    }

    @Override
    public Integer getIntegerFlag(String key) {
        Object value = getClaim(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    @Override
    public Boolean getBooleanFlag(String key) {
        Object value = getClaim(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }
}