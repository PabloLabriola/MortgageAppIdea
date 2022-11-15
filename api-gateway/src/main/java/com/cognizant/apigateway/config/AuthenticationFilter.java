package com.cognizant.apigateway.config;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtUtils jwtUtils;
    private final String BEARER = "Bearer ";


    public AuthenticationFilter(RouterValidator routerValidator,
        JwtUtils jwtUtils) {
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            Optional<String> token = this.getAuthHeader(request);
            if (token.isPresent() && isBearerToken(token.get())) {
                if (jwtUtils.isInvalid(token.get().replaceAll(BEARER, "")))
                    return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            } else {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }


    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private Optional<String> getAuthHeader(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().getOrEmpty(("Authorization"));
        if (headers.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(headers.get(0));
        }


    }

    private boolean isBearerToken(String token) {
        return token.startsWith(BEARER);
    }
}