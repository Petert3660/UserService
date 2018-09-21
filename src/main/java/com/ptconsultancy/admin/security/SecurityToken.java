package com.ptconsultancy.admin.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityToken {

    private String value;
    private boolean tokenLock;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTokenLock() {
        return tokenLock;
    }

    public void setTokenLock(boolean tokenLock) {
        this.tokenLock = tokenLock;
    }
}
