package com.datarelay.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.dto.UserDTO;
import com.datarelay.core.service.rest.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Tag(name = "Authentication", description = "User authentication REST controller")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @Operation(summary = "User login", description = "Handles user credentials and logs in user, returning a token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User logged in"),
        @ApiResponse(responseCode = "401", description = "Invalid user credentials", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Invalid credentials")))
    })
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
