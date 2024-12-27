package com.socompany.apigateway.filter;

import com.socompany.apigateway.util.JwtValidator;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

// Перевірка даних проходить лише в Security Service - тому цей фільтр є непотрібним...

@Component
public class AdminAuthenticationFilter extends AbstractGatewayFilterFactory<AdminAuthenticationFilter.Config> {
    @Autowired
    private JwtValidator jwtValidator;

    private final Logger LOGGER = LoggerFactory.getLogger(AdminAuthenticationFilter.class);

    @Autowired
    private RouteValidator routeValidator;

    public AdminAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            LOGGER.info("Received request for admin authentication");
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    LOGGER.debug("Request is missing authorization header");

                    return null; // Throw error then
                } else {
                    LOGGER.debug("Trying to execute auth header and role");
                    // Continue 
                }
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}
