package com.datarelay.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.datarelay.core.security.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http) {
        return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers("/auth/**").permitAll()
            .pathMatchers("/user/create").permitAll()
            .pathMatchers("/schema/**").authenticated()
            .pathMatchers("/ws/**").authenticated()
            .anyExchange().authenticated()
        )
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .build();
    }
}
