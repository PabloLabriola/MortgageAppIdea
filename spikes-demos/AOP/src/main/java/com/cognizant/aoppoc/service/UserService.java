package com.cognizant.aoppoc.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public boolean login(String userName, String userPass){
        if (userName.equals("admin") && userPass.equals("123456")) {
            return true;
        } else {
            return false;
        }
    }

    public void doThrowingException(){
        throw new RuntimeException("123 - Throwing RuntimeException");
    }
}
