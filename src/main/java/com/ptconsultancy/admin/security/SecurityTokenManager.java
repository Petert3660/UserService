package com.ptconsultancy.admin.security;

import com.ptconsultancy.admin.adminsupport.ControllerConstants;
import com.ptconsultancy.domain.utilities.GenerateRandomKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityTokenManager {

    private SecurityToken securityToken;

    @Autowired
    public SecurityTokenManager(SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    public String getValue() {
        return securityToken.getValue();
    }

    public boolean isTokenLock() {
        return securityToken.isTokenLock();
    }

    public void resetToken() {
        this.securityToken.setTokenLock(false);
        this.securityToken.setValue(GenerateRandomKeys.generateRandomKey(ControllerConstants.TOKEN_LENGTH, ControllerConstants.TOKEN_MODE));
    }

    public void setToken() {
        this.securityToken.setTokenLock(true);
        this.securityToken.setValue(GenerateRandomKeys.generateRandomKey(ControllerConstants.TOKEN_LENGTH, ControllerConstants.TOKEN_MODE));
    }
}
