package com.datarelay.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.entity.User;
import com.datarelay.core.service.rest.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class DataController {

    @Autowired
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "DataRelay API endpoint test";
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        userService.createNewUser(
            userRequest.getUsername(),
            userRequest.getPassword()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    
}