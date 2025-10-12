// src/main/java/com/datarelay/core/config/SecurityConfig.java
package com.datarelay.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  SecurityFilterChain security(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/ws/**", "/app/**", "/topic/**").permitAll()
        .anyRequest().permitAll()
      )
      .httpBasic(Customizer.withDefaults())
      .build();
  }
}
