package com.datarelay.core.security;

import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.lang.Collections;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpCookie authCookie = exchange.getRequest().getCookies().getFirst("AUTH-TOKEN");

        if (authCookie != null) {
            String token = authCookie.getValue();
            if (jwtService.validateToken(token)) {
                String id = jwtService.extractId(token);

                // Spring security auth obj
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(id, null, Collections.emptyList());

                return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            }
        }

        return chain.filter(exchange);
    }
}
