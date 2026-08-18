package com.datarelay.core.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.entity.User;
import com.datarelay.core.service.rest.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/create")
    public Mono<ResponseEntity<Object>> createUser(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        return userServiceImpl.createNewUser(username, password)
            .map(newUser -> {
                return ResponseEntity.status(HttpStatus.CREATED).body((Object) newUser);
            })
            .onErrorResume(error -> {
                log.error("Service error on createUser: {}", error.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists"));
            });
    }
}
