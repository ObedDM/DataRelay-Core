package com.datarelay.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.entity.User;
import com.datarelay.core.service.rest.UserServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class DataController {

    @Autowired
    private UserServiceImpl UserService;

    @GetMapping("/")
    public String home() {
        return "DataRelay API endpoint test";
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        UserService.createNewUser(
            userRequest.getUsername(),
            userRequest.getPassword()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    
}