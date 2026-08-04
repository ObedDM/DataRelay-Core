package com.datarelay.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.datarelay.core.security.JwtAuthFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers("/auth/**").permitAll()
            .pathMatchers("/ws/**").authenticated()
            .anyExchange().authenticated()
        )
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .build();
    }
}
