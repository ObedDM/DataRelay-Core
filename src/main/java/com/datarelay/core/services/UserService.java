package com.datarelay.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datarelay.core.entity.Users;
import com.datarelay.core.repository.sql.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public Users createNewUser(String username, String password) {
        Users newUser = new Users();

        newUser.setUsername(username);
        newUser.setPassword(password);

        return userRepository.save(newUser);
    }
}