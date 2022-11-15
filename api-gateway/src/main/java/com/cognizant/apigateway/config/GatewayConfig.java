package com.cognizant.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("mortgagefeasibility", r -> r.path("/mortgagefeasibility/validate/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://mortgagefeasibility"))
                .route("mortgagefeasibility", r -> r.path("/mortgagefeasibility/confirm/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://mortgagefeasibility"))
                .route("mortgagefeasibility", r -> r.path("/mortgagefeasibility/h2-console/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://mortgagefeasibility"))
                .route("mortgagefeasibility", r -> r.path("/mortgagefeasibility/actuator/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://mortgagefeasibility"))
                .route("companymanagement", r -> r.path("/companymanagement/auth/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://companymanagement"))
                .route("companymanagement", r -> r.path("/companymanagement/actuator/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://companymanagement"))
                .build();
    }

}
