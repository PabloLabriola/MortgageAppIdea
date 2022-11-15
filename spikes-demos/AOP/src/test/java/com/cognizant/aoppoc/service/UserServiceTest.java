package com.cognizant.aoppoc.service;

import com.cognizant.aoppoc.AopPocApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testLogin(){
        Boolean result= this.userService.login("admin", "123456");
        assertEquals(true, result);
    }

    @Test
    public void testThrowingException(){
        Throwable exception= assertThrows(RuntimeException.class, ()->{
            this.userService.doThrowingException();
        });
        String expectedMessage= "123 - Throwing RuntimeException";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}