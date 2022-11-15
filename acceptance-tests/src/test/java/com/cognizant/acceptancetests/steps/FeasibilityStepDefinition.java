package com.cognizant.acceptancetests.steps;

import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import utilities.Constants;
import utilities.RestAssuredExtension;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEmptyString.emptyOrNullString;

public class FeasibilityStepDefinition {

    private ResponseOptions<Response> response;
    private HashMap<String, String> headers;
    private HashMap<String, String> body;
    private String company;
    private String token;


    @BeforeStep()
    public void setUp() {
        token = SecurityMechanismStepDefinition.getToken();
        company = SecurityMechanismStepDefinition.getCompany();
    }

    @Given("A client with monthly incomes of {string}")
    public void a_client_with_monthly_incomes_of(String monthlyIncomes) {
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
        this.headers.put("Authorization", token);
        this.body.put("monthlyIncomes", monthlyIncomes);
        this.body.put("company", Constants.COMPANY_SUBSCRIBED);
    }

    @Given("A client with monthly incomes {double} and home price {double}")
    public void aClientWithMonthlyIncomesAndHomePrice(Double monthlyIncomes, Double homePrice) {
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
        this.headers.put("Authorization", token);
        this.body.put("monthlyIncomes", monthlyIncomes.toString());
        this.body.put("homePrice", homePrice.toString());
        this.body.put("company", Constants.COMPANY_SUBSCRIBED);
    }

    @When("^A 3rd party request the feasibility study$")
    public void a3rdPartyRequestFeasibilityStudy() {
        response = RestAssuredExtension
                .postOperationWithHeaderAndBody(Constants.FEASIBILTY_URI, headers, body);
    }

    @Then("^mortgage is approved and provides fixed and variable mortgage details$")
    public void mortgageIsApprovedWithTwoMortgages() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(Constants.APPROVED))
                .body("mortgages.size()", is(2))
                .body("mortgages[0].id",not(emptyOrNullString()))
                .body("mortgages[1].id",not(emptyOrNullString()));
    }

    @Then("^mortgage is approved and provides fixed mortgage details$")
    public void mortgageIsApprovedWithOneMortgage() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(Constants.APPROVED))
                .body("mortgages.size()", is(1))
                .body("mortgages[0].interestType", equalTo("FIXED"))
                .body("mortgages[0].id",not(emptyOrNullString()));
    }

    @Then("^mortgage is rejected$")
    public void mortgageIsRejected() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(Constants.REJECTED));
    }

    @Then("^mortgage is on hold$")
    public void mortgageIsOnHold() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(Constants.HOLD));
    }

    @Then("mortgage is approved")
    public void mortgage_is_approved() {
        response.andReturn().then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("state", equalTo(Constants.APPROVED));
    }
}
