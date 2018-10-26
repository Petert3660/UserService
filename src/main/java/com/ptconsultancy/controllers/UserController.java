package com.ptconsultancy.controllers;

import static com.ptconsultancy.application.ApplicationConstants.SUPERUSER;

import com.ptconsultancy.admin.adminsupport.ControllerConstants;
import com.ptconsultancy.admin.security.SecurityTokenManager;
import com.ptconsultancy.entities.User;
import com.ptconsultancy.messages.PropertiesHandler;
import com.ptconsultancy.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserRepository userRepository;

    private SecurityTokenManager securityTokenManager;

    private PropertiesHandler propertiesHandler;

    @Autowired
    public UserController(UserRepository userRepository, SecurityTokenManager securityTokenManager,
        PropertiesHandler propertiesHandler) {
        this.userRepository = userRepository;
        this.securityTokenManager = securityTokenManager;
        this.propertiesHandler = propertiesHandler;
    }

    @RequestMapping(path="/getUser/{username}/{token}", method=RequestMethod.GET)
    public User getUser(@PathVariable String username, @PathVariable String token) {

        User output = null;

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();
            List<User> users = userRepository.findByUsername(username);
            if (users.size() == 1) {
                output = users.get(0);
            }
        }

        return output;
    }

    @RequestMapping(path="/getAllUsers/{username}/{userId}/{pass}/{token}", method=RequestMethod.GET)
    public List<User> getAllUsers(@PathVariable String username, @PathVariable String userId,
        @PathVariable String pass, @PathVariable String token) {

        List<User> output = null;

        if (username.equals(SUPERUSER)) {
            if (token.equals(securityTokenManager.getValue())) {
                securityTokenManager.resetToken();
                if (userId.equals(propertiesHandler.getProperty(ControllerConstants.ID_KEY)) && pass.equals(propertiesHandler.getProperty(ControllerConstants.PASS_KEY))) {
                    output = (List<User>) userRepository.findAll();
                }
            }
        }

        return output;
    }

    @RequestMapping(path="/createUser/{userId}/{pass}/{token}", method=RequestMethod.POST)
    public String saveUser(@RequestBody() User user, @PathVariable String userId, @PathVariable String pass, @PathVariable String token) {

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();

            for (User userEntity : userRepository.findAll()) {
                if (user.getUsername().equals(userEntity.getUsername())) {
                    return "User already exists - cannot add";
                }
            }

            User createdUser = new User(user.getUsername(), user.getPassword(), user.getRole());

            if (userId.equals(propertiesHandler.getProperty(ControllerConstants.ID_KEY)) && pass.equals(propertiesHandler.getProperty(ControllerConstants.PASS_KEY))) {
                userRepository.save(createdUser);
                return "New user successfully added";
            } else {
                return "Authentication failed - new user not added";
            }
        } else {
            return "Security check failed - new user not added";
        }
    }

    @RequestMapping(path="/deleteUser/{username}/{userId}/{pass}/{token}", method=RequestMethod.DELETE)
    public String removeUser(@PathVariable String username, @PathVariable String userId, @PathVariable String pass,
         @PathVariable String token) {

        if (token.equals(securityTokenManager.getValue())) {
            securityTokenManager.resetToken();
            if (userId.equals(propertiesHandler.getProperty(ControllerConstants.ID_KEY)) && pass.equals(propertiesHandler.getProperty(ControllerConstants.PASS_KEY))) {
                List<User> entities = userRepository.findByUsername(username);
                if (entities.size() == 1) {
                    userRepository.delete(entities.get(0));
                    return "User deleted";
                } else {
                    return "Could not find user";
                }
            } else {
                return "Authentication failed - user nor deleted";
            }
        } else {
            return "Security check failed - user not deleted";
        }
    }
}
