package com.datarelay.core.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.security.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    
    @PostMapping("/login")
    public Mono<String> login(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username"); // change for id
        String token = jwtService.generateToken(username);

        ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", token)
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(Duration.ofDays(1))
            .build();

        /* return Mono.just(ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .build());
        */

            return Mono.just(token);
    }
}
