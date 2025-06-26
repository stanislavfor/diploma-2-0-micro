package com.example.api.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8081"))
                .route("image-hosting", r -> r.path("/api/images/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("http://localhost:8084"))
                .route("frontend", r -> r.path("/", "/index.html", "/css/**", "/js/**", "/favicon.ico")
                        .uri("http://localhost:8084"))
                .build();
    }
}
