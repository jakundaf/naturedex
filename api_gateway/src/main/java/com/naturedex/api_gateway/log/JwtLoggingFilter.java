package com.naturedex.api_gateway.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtLoggingFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtLoggingFilter.class);

    // Lista endpointów, które nie będą logowane ani filtrowane (publiczne)
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // pomijamy filtrowanie dla publicznych endpointów
        if (PUBLIC_PATHS.contains(path)) {
            return chain.filter(exchange);
        }

        // Logujemy token JWT z nagłówka Authorization (opcjonalnie)
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("JWT token passed to gateway: {}", token);
        } else {
            log.warn("No valid JWT token in request to {}", path);
        }

        return chain.filter(exchange);
    }
}


