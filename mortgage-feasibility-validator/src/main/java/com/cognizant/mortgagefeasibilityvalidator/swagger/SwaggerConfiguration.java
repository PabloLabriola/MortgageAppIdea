package com.cognizant.mortgagefeasibilityvalidator.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfiguration {

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
    @Bean
        public Docket api() {
            return new Docket(DocumentationType.OAS_30)
                    .securityContexts(Arrays.asList(securityContext()))
                    .securitySchemes(Arrays.asList(apiKey()))
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.cognizant.mortgagefeasibilityvalidator.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(getApiInfo());
        }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Mortgage Feasibility API",
                "Mortgage Feasibility API",
                "1.0",
                "",
                new Contact("Cognizant", "", ""),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }
}
