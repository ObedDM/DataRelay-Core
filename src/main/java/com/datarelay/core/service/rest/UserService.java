package com.datarelay.core.service.rest;

import com.datarelay.core.entity.User;

import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<User> createNewUser(String username, String password);

    public Mono<String> login(String username, String password);
}
