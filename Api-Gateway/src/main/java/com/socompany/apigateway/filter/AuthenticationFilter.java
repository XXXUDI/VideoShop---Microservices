package com.socompany.apigateway.filter;

import com.socompany.apigateway.util.JwtValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtValidator jwtValidator;

    @Autowired
    private RouteValidator validator;



    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            LOGGER.info("Received request for authentication: {}", exchange.getRequest().getURI());
            if (validator.isSecured.test(exchange.getRequest())) {
                LOGGER.debug("{} is secured", exchange.getRequest().getURI());
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    LOGGER.debug("Request is missing Authorization header");
                    return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                } else {
                    LOGGER.debug("Trying to execute authentication headers (JWT)");
                    String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        authHeader = authHeader.substring(7);
                        LOGGER.debug("Received Bearer header: {}", authHeader);
                    }
                    try {
                        LOGGER.debug("Trying to validate JWT token using SECURITY-SERVICE");
                        jwtValidator.validateToken(authHeader);
                    } catch (Exception e) {
                        LOGGER.error("Failed to validate JWT token", e);
                        return onError(exchange, "Unauthorized access to application.", HttpStatus.UNAUTHORIZED);
                    }
                }
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(("{\"error\":\"" + errorMessage + "\"}").getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    public static class Config {


    }

}
