package com.cognizant.acceptancetests.steps;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.apache.http.HttpStatus;
import utilities.Constants;
import utilities.RestAssuredExtension;

import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class MortgageConfirmStepDefinition {
    private static ResponseOptions<Response> response;
    private String company;
    private String token;
    private String mortgageId;
    private HashMap<String, String> headers;
    private HashMap<String, String> body;

    @BeforeStep()
    public void setUp() {
        token = SecurityMechanismStepDefinition.getToken();
        company = SecurityMechanismStepDefinition.getCompany();
    }


    @Then("Other mortgages linked to the same request will be cancelled")
    public void other_mortgages_cancelled() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("mortgages.size()", is(2))
                .body("mortgages[1].state", is(Constants.CANCELLED));
    }

    @Given("A third party has a cancelled mortgage id")
    public void third_party_has_cancelled_mortgage() {
        headers = new HashMap<>();
        body = new HashMap<>();
        body.put("monthlyIncomes", "101.0");
        body.put("homePrice", "99999.9");
        body.put("company", Constants.COMPANY_SUBSCRIBED);
        headers.put("Authorization", token);
        response = RestAssuredExtension
                .postOperationWithHeaderAndBody(Constants.FEASIBILTY_URI, headers, body);

        mortgageId = response.getBody().jsonPath().get("mortgages[0].id").toString();

        HashMap<String, String> params = new HashMap<>();
        headers = new HashMap<>();
        params.put("mortgageId", mortgageId);
        headers.put("Authorization", token);
        response = RestAssuredExtension
                .postOperationWithParamsAndHeaders(Constants.CONFIRM_URI, headers, "mortgageId", mortgageId);

        mortgageId = response.getBody().jsonPath().get("mortgages[1].id").toString();

    }

    @Then("The system returns a 409 error")
    public void response_409_error() {
        assertEquals(HttpStatus.SC_CONFLICT, response.getStatusCode());
    }

    @Given("A third party has a non existing mortgage id")
    public void third_party_has_non_existing_mortgage_id() {
        mortgageId = "00000000-0000-0000-0000-000000000000";
    }

    @Then("The service will return a 404 error")
    public void response_404_error() {
        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Given("A third party has an invalid mortgage id")
    public void third_party_has_invalid_mortgage_id() {
        mortgageId = "X";
    }

    @Given("A third party has an existing mortgage id")
    public void third_party_have_existing_mortgage_id() {
        headers = new HashMap<>();
        body = new HashMap<>();
        body.put("monthlyIncomes", "101.0");
        body.put("homePrice", "99999.9");
        body.put("company", Constants.COMPANY_SUBSCRIBED);
        headers.put("Authorization", token);
        response = RestAssuredExtension
                .postOperationWithHeaderAndBody(Constants.FEASIBILTY_URI, headers, body);

        mortgageId = response.getBody().jsonPath().get("mortgages[0].id").toString();
    }


    @When("Tries to confirm that mortgage")
    public void confirm_mortgage() {
        HashMap<String, String> params = new HashMap<>();
        headers = new HashMap<>();
        params.put("mortgageId", mortgageId);
        headers.put("Authorization", token);
        response = RestAssuredExtension
                .postOperationWithParamsAndHeaders(Constants.CONFIRM_URI, headers, "mortgageId", mortgageId);
    }


    @Then("Status of mortgage changes to confirmed")
    public void mortgage_confirmed() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("mortgages.size()", is(2))
                .body("mortgages[0].state", is(Constants.CONFIRMED));
    }

    @Then("The service will return a 400 error")
    public void response_400_error() {
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
        assertEquals("The mortgage id does not follow the correct UUID format.", ((RestAssuredResponseImpl) response).asString());
    }
}