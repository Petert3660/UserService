package com.ptconsultancy.admin.restcontrollers;

import com.ptconsultancy.admin.adminsupport.BuildVersion;
import com.ptconsultancy.admin.adminsupport.ControllerConstants;
import com.ptconsultancy.admin.security.SecurityTokenManager;
import com.ptconsultancy.messages.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private static final String ADMINID_TOGGLE = "adminid.allowed";
    private static final String ADMINPASS_TOGGLE = "adminpass.allowed";
    private static final String SHUTDOWN_TOGGLE = "shutdown.allowed";

    private static final String CREDS_DISABLED_MESSAGE = "This facility has been disabled";

    private MessageHandler messageHandler;

    private SecurityTokenManager securityTokenManager;

    private Environment env;

    private boolean adminidToggle = false;
    private boolean adminpassToggle = false;
    private boolean shutdownToggle = false;

    @Autowired
    public AdminController(MessageHandler messageHandler, SecurityTokenManager securityTokenManager, Environment env) {
        this.messageHandler = messageHandler;
        this.securityTokenManager = securityTokenManager;
        this.env = env;
        if (this.env.getProperty(ADMINID_TOGGLE).equals("true")) {
            adminidToggle = true;
        }
        if (this.env.getProperty(ADMINPASS_TOGGLE).equals("true")) {
            adminpassToggle = true;
        }
        if (this.env.getProperty(SHUTDOWN_TOGGLE).equals("true")) {
            shutdownToggle = true;
        }
    }

    @RequestMapping(path="/healthcheck", method=RequestMethod.GET)
    public String healthcheck() {
        if (BuildVersion.getProjectTitle() != null) {
            return BuildVersion.getProjectTitle() + " is running OK";
        } else {
            return "Application is running OK";
        }
    }

    @RequestMapping(path="/shutdown/{id}/{pass}", method=RequestMethod.POST)
    public void shutdown(@PathVariable String id, @PathVariable String pass) {
        if (id.equals(messageHandler.getMessage(ControllerConstants.ID_KEY)) && pass.equals(messageHandler.getMessage(ControllerConstants.PASS_KEY))
            && shutdownToggle) {
            securityTokenManager.resetToken();
            System.exit(ControllerConstants.EXIT_STATUS);
        }
    }

    @RequestMapping(path="/securitytoken", method=RequestMethod.GET)
    public String getSecurityToken() {

        if (!securityTokenManager.isTokenLock()) {
            securityTokenManager.setToken();
            return securityTokenManager.getValue();
        } else {
            return ControllerConstants.NO_TOKEN_MESSAGE;
        }
    }

    @RequestMapping(path="getadminid/{token}", method=RequestMethod.GET)
    public String getAdminId(@PathVariable String token) {

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();
            if (adminidToggle) {
                return messageHandler.getMessage(ControllerConstants.ID_KEY);
            } else {
                return CREDS_DISABLED_MESSAGE;
            }
        } else {
            return ControllerConstants.NO_TOKEN_MESSAGE;
        }
    }


    @RequestMapping(path="getadminpass/{token}", method=RequestMethod.GET)
    public String getAdminPass(@PathVariable String token) {

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();
            if (adminpassToggle) {
                return messageHandler.getMessage(ControllerConstants.PASS_KEY);
            } else {
                return CREDS_DISABLED_MESSAGE;
            }
        } else {
            return ControllerConstants.NO_TOKEN_MESSAGE;
        }
    }
}
