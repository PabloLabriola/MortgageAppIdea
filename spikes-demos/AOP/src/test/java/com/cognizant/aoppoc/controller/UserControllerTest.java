package com.cognizant.aoppoc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHome(){
        String testUrl= "/";
        ResponseEntity<String> entity= this.restTemplate.getForEntity(testUrl, String.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(entity.getBody(), "Welcome to Spring Boot!");
    }
}