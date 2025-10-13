package com.datarelay.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.entity.Users;
import com.datarelay.core.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class DataController {

    @Autowired
    private UserService UserService;

    @GetMapping("/")
    public String home() {
        return "DataRelay API endpoint test";
    }

    @PostMapping("/create-user")
    public ResponseEntity<Users> createUser(@RequestBody Users userRequest) {
        UserService.createNewUser(
            userRequest.getUsername(),
            userRequest.getPassword()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    
}