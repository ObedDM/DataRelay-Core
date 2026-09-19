package com.datarelay.core.service.rest;

import com.datarelay.core.entity.User;

import reactor.core.publisher.Mono;

public interface UserService {

    /**
     * Saves a new user in the database
     * 
     * @param username unique new user handle
     * @param password plaintext password to hash and store
     * @return a Mono with the newly created {@link User}
     */
    public Mono<User> createNewUser(String username, String password);

    /**
     * Logs in user

     * @param username unique user handle
     * @param password plaintext user password
     * @return a Mono containing JWT as a String
     */
    public Mono<String> login(String username, String password);
}
