package com.cognizant.acceptancetests.steps;

import io.cucumber.java.Before;
import utilities.RestAssuredExtension;

public class TestInitialize {

    @Before
    public void setUp(){
        RestAssuredExtension restAssuredExtension = new RestAssuredExtension();
    }
}
