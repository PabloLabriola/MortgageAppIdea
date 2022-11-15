package com.cognizant.aoppoc.controller;

import com.cognizant.aoppoc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home(){
        if (this.userService.login("admin", "123456")) {
            return "Welcome to Spring Boot!";
        } else {
            return "Login failed, please try again!";
        }
    }
}
