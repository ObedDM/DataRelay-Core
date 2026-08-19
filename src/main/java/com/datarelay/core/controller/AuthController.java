package com.datarelay.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.dto.UserDTO;
import com.datarelay.core.service.rest.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@Valid @RequestBody UserDTO credentials) {
        String username = credentials.username();
        String password = credentials.password();
        
        return userService.login(username, password)
            .map(token -> {
                return ResponseEntity.status(HttpStatus.OK).body(token); //remove token from body when setting cookie
            })
            .onErrorResume(error -> {
                log.error("Service error on login: {}", error.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
            });
    }
}
