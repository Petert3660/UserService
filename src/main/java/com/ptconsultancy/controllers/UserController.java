package com.ptconsultancy.controllers;

import com.ptconsultancy.admin.security.SecurityTokenManager;
import com.ptconsultancy.entities.User;
import com.ptconsultancy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserRepository userRepository;

    private SecurityTokenManager securityTokenManager;

    @Autowired
    public UserController(UserRepository userRepository, SecurityTokenManager securityTokenManager) {
        this.userRepository = userRepository;
        this.securityTokenManager = securityTokenManager;
    }

    @RequestMapping(path="/getUser/{username}/{token}", method=RequestMethod.GET)
    public User getUser(@PathVariable String username, @PathVariable String token) {

        User output = null;

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();
            output = userRepository.findByUsername(username).get(0);
        }

        return output;
    }
}
