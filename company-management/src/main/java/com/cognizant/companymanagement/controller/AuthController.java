package com.cognizant.companymanagement.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import com.cognizant.companymanagement.security.model.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.companymanagement.security.jwt.JwtUtils;
import com.cognizant.companymanagement.services.CompanyService;

/**
 * Authentication controller
 */
@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtils jwtUtils;

    private final CompanyService companyService;

    public AuthController(JwtUtils jwtUtils, CompanyService companyService) {
        this.jwtUtils = jwtUtils;
        this.companyService = companyService;
    }

    /**
     * Given a company name, an access token is returned if it exists, if not an
     * error 401 is thrown
     *
     * @param companyRequest
     * @return
     */

    @PostMapping("/token")
    @ApiOperation(value = "authenticateCompany", notes = "Given a company name, an access token is returned if it exists, if not an error 401 is thrown")
    public ResponseEntity<?> authenticateCompany(@Valid @RequestBody CompanyRequest companyRequest) {
		log.info("Received request to authenticate from: " + companyRequest.getCompanyName());
		if (companyService.isSubscribed(companyRequest.getCompanyName())) {
            String accessToken = jwtUtils.getJWTAccessToken(companyRequest.getCompanyName());
            String refreshToken = jwtUtils.getJWTRefreshToken(companyRequest.getCompanyName());
            Authentication authentication = new UsernamePasswordAuthenticationToken(companyRequest.getCompanyName(),
                    null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String companyName = (String) authentication.getPrincipal();
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken, companyName));
        } else {
            ResponseEntity result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtError(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.name(),
                    "Company name not found!",
                    companyRequest.getCompanyName()));

            return result;
        }
    }

    /**
     * Given a refresh token, an access token is returned if it's valid, if not an
     * http status 417 is returned
     *
     * @param tokenRefreshRequest
     * @return
     */

    @PostMapping("/refreshtoken")
    @ApiOperation(value = "refreshtoken", notes = "Given a refresh token, an access token is returned if it's valid, if not an http status 417 is returned")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
		log.info("Received request to refresh token for token: " + tokenRefreshRequest.getRefreshToken());
		String requestRefreshToken = tokenRefreshRequest.getRefreshToken().replaceAll(BEARER_PREFIX, "");
        switch (jwtUtils.checkRefreshToken(requestRefreshToken)) {
            case VALID:
                String newAccessToken = jwtUtils.getJWTAccessToken(jwtUtils.getCompanyNameFromJwtRefreshToken(requestRefreshToken));
                return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, requestRefreshToken));
            case EXPIRED:
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        JwtError.builder()
                                .errorCode(HttpStatus.EXPECTATION_FAILED.value())
                                .errorLabel(HttpStatus.EXPECTATION_FAILED.name())
                                .errorMessage("Refresh token has expired. Please make a new access token request")
                                .value(requestRefreshToken)
                                .build());
            case INVALID:
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        JwtError.builder()
                                .errorCode(HttpStatus.EXPECTATION_FAILED.value())
                                .errorLabel(HttpStatus.EXPECTATION_FAILED.name())
                                .errorMessage("Refresh token is not valid. Please make a new access token request")
                                .value(requestRefreshToken)
                                .build());
            default:
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        JwtError.builder()
                                .errorCode(HttpStatus.EXPECTATION_FAILED.value())
                                .errorLabel(HttpStatus.EXPECTATION_FAILED.name())
                                .errorMessage("Refresh token is malformed. Please make a new access token request")
                                .value(requestRefreshToken)
                                .build());
        }
    }

    /**
     * Given an access token, it returns 200 http status if it's valid or 401 if not
     *
     * @param accessTokenRequest
     * @return
     */
    @PostMapping("/token/validate")
    @ApiOperation(value = "validateAccessToken", notes = "Given an access token, it returns 200 http status if it's valid or 401 if not")
    public ResponseEntity<?> validateAccessToken(@Valid @RequestBody AccessTokenRequest accessTokenRequest) {
        String accessToken = accessTokenRequest.getAccessToken();
        switch (jwtUtils.checkAccessToken(accessToken)) {
            case VALID:
                return ResponseEntity.ok().build();
            default:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
