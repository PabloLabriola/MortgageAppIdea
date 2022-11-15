package com.cognizant.companymanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.cognizant.companymanagement.security.model.AccessTokenRequest;
import com.cognizant.companymanagement.security.model.CompanyRequest;
import com.cognizant.companymanagement.security.model.TokenRefreshRequest;

import io.restassured.RestAssured;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTests {
	@LocalServerPort
	private int port;
	
	@BeforeEach
	public void setUp() throws Exception {
	    RestAssured.port = port;
	}
		
	@Test
    public void shouldReturnToken() {
		given()
		.contentType("application/json")
		.body(new CompanyRequest().companyName("CHOAM"))
		.when()
		.post("/auth/token")
		.then()
		.statusCode(200)
		.body("companyName", equalTo("CHOAM"))
		.body(containsStringIgnoringCase("token"));
	}
	
	@Test
    public void shouldUnauthorizeCompany() {
		given()
		.contentType("application/json")
		.body(new CompanyRequest().companyName("notRegistred"))
		.when()
		.post("/auth/token")
		.then()
		.statusCode(401)
		.body("errorMessage", containsString("Company name not found!"));
	}
	
	@Test
    public void shouldReturnNewAccessToken() {
		// Retrieve a refresh token
		String refreshToken = given()
		.contentType("application/json")
		.body(new CompanyRequest().companyName("CHOAM"))
		.when()
		.post("/auth/token")
		.then()
		.extract()
		.path("refreshToken");
		
		// Get a new access token
		given()
		.contentType("application/json")
		.body(new TokenRefreshRequest().refreshToken(refreshToken))
		.when()
		.post("/auth/refreshtoken")
		.then()
		.statusCode(200)
		.body("refreshToken", equalTo(refreshToken))
		.body(containsString("accessToken"));
	}
	
	@Test
    public void shouldReturnMalformedToken() {
		given()
		.contentType("application/json")
		.body(new TokenRefreshRequest().refreshToken("anything"))
		.when()
		.post("/auth/refreshtoken")
		.then()
		.statusCode(417)
		.body("errorMessage", containsString("Refresh token is malformed."))
		.body(containsString("errorCode"));
	}
	
	@Test
    public void shouldValidateAccessToken() {
		String accessToken = given()
		.contentType("application/json")
		.body(new CompanyRequest().companyName("CHOAM"))
		.when()
		.post("/auth/token")
		.then()
		.extract()
		.path("accessToken");
		
		given()
		.contentType("application/json")
		.body(new AccessTokenRequest().accessToken(accessToken.substring(7)))
		.when()
		.post("/auth/token/validate")
		.then()
		.statusCode(200);
	}
	
	@Test
    public void shouldInvalidateAccessToken() {
		given()
		.contentType("application/json")
		.body(new AccessTokenRequest().accessToken("anything"))
		.when()
		.post("/auth/token/validate")
		.then()
		.statusCode(401);
	}

}
