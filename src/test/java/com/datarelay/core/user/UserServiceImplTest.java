package com.datarelay.core.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.datarelay.core.entity.User;
import com.datarelay.core.repository.sql.UserRepository;
import com.datarelay.core.service.rest.UserServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    public String PASSWORD = "password123";
    public String HASHED_PASSWORD = "$2a$12$DSlDCwObzcc7DUoQ5Zp3A.0erlHOk32OKMcmusu/11yMSPgh3N.yW";

    public User USER = User.builder()
        .username("myUser")
        .password(HASHED_PASSWORD)
        .build();

    @Test
    void createNewUser_WhenUsernameDoesNotExist_ReturnSavedUser() {
        when(userRepository.existsByUsername(USER.getUsername()))
            .thenReturn(Mono.just(false));

        when(encoder.encode(PASSWORD))
            .thenReturn(HASHED_PASSWORD);

        when(userRepository.save(USER))
            .thenReturn(Mono.just(USER));

        StepVerifier.create(userService.createNewUser(USER.getUsername(), PASSWORD))
            .expectNextMatches(savedUser -> 
                savedUser.getUsername().equals("myUser") &&
                savedUser.getPassword().equals(USER.getPassword()))
            .verifyComplete();

        verify(userRepository).save(USER);
    }

    @Test
    void createNewUser_WhenUsernameExists_ThrowsException() {
        when(userRepository.existsByUsername(USER.getUsername()))
            .thenReturn(Mono.just(true));

        StepVerifier.create(userService.createNewUser(USER.getUsername(), PASSWORD))
            .expectErrorMessage("Username already exists")
            .verify();
        
        verify(encoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
