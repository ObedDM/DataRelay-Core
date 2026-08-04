package com.datarelay.core.service.rest;

import org.springframework.stereotype.Service;

import com.datarelay.core.entity.User;
import com.datarelay.core.repository.sql.UserRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Mono<User> createNewUser(String username, String password) {
        User newUser = new User();

        newUser.setUsername(username);
        // hash password here
        newUser.setPassword(password);

        return userRepository.save(newUser);
    }
}