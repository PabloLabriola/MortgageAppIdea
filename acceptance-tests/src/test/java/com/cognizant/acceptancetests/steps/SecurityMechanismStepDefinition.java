package com.cognizant.acceptancetests.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import utilities.Constants;
import utilities.RestAssuredExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityMechanismStepDefinition {

    private static ResponseOptions<Response> response;

    private static String token;
    private static String company;

    public static String getToken() {
        return token;
    }

    public static String getCompany(){
        return company;
    }

    @Given("3rd party provides correct credentials")
    public void rd_party_authenticates_to_as() {
        var body = new HashMap<String,String>();
        body.put("companyName",Constants.COMPANY_SUBSCRIBED);
        response = RestAssuredExtension
                .postOperationWithBody(Constants.AUTHENTICATION_URI,body);
        company= Constants.COMPANY_SUBSCRIBED;
    }

    @Then("3rd party get authenticated")
    public void rd_party_get_authenticated() {
        token = response.getBody().jsonPath().get("accessToken").toString();
        assertThat(token)
                .isNotEmpty()
                .isNotBlank();
    }
    @Given("3rd party provides incorrect credentials")
    public void companyProvidesIncorrectCredentials() {
        var body = new HashMap<String,String>();
        body.put("companyName",Constants.COMPANY_NOT_SUBSCRIBED);
        response = RestAssuredExtension
                .postOperationWithBody(Constants.AUTHENTICATION_URI,body);
        company= Constants.COMPANY_NOT_SUBSCRIBED;
    }

    @Then("3rd party doesn't get authenticated")
    public void rd_party_doesn_t_get_authenticated() {

        assertThat(response.getStatusCode()).isEqualTo(401);
    }
}
