package com.cognizant.acceptancetests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}
        , publish = true
        , features = {"src/test/java/com/cognizant/acceptancetests/features"})
public class RunCucumberTest {

}
