package com.datarelay.core.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.datarelay.core.entity.User;
import com.datarelay.core.repository.sql.UserRepository;
import com.datarelay.core.security.JwtService;
import com.datarelay.core.service.rest.UserServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock 
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    public String PASSWORD = "password123";
    public String HASHED_PASSWORD = "$2a$12$DSlDCwObzcc7DUoQ5Zp3A.0erlHOk32OKMcmusu/11yMSPgh3N.yW";
    public UUID USER_ID = UUID.randomUUID();
    public String TOKEN = "token-" + USER_ID.toString();

    public User USER = User.builder()
        .username("myUser")
        .password(HASHED_PASSWORD)
        .build();

    @BeforeEach
    void setUp() {
        USER.setUserId(null); // this is done as first login test change the value of the User's userId
    }

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

    // change this to returns cookie later on as returns token is only for development atm
    @Test
    void login_WhenUserFoundAndPasswordMatches_ReturnsToken() {
        USER.setUserId(USER_ID);

        when(userRepository.findByUsername(USER.getUsername()))
            .thenReturn(Mono.just(USER));

        when(encoder.matches(PASSWORD, HASHED_PASSWORD))
            .thenReturn(true);

        when(jwtService.generateToken(USER_ID.toString()))
            .thenReturn(TOKEN);

        StepVerifier.create(userService.login(USER.getUsername(), PASSWORD))
            .expectNext(TOKEN)
            .verifyComplete();

        verify(userRepository).findByUsername(USER.getUsername());
        verify(encoder).matches(PASSWORD, HASHED_PASSWORD);
        verify(jwtService).generateToken(USER_ID.toString());
    }

    @Test 
    void login_WhenUserFoundAndPasswordDoesNotMatch_ThrowsException() {

        when(userRepository.findByUsername(USER.getUsername()))
            .thenReturn(Mono.just(USER));

        when(encoder.matches(PASSWORD, HASHED_PASSWORD))
            .thenReturn(false);

        StepVerifier.create(userService.login(USER.getUsername(), PASSWORD))
            .expectErrorMessage("Invalid credentials")
            .verify();

        verify(userRepository).findByUsername(USER.getUsername());
        verify(encoder).matches(PASSWORD, HASHED_PASSWORD);
        verifyNoInteractions(jwtService);
    }

    @Test 
    void login_WhenUserNotFound_ThrowsException() {
        when(userRepository.findByUsername(USER.getUsername()))
            .thenReturn(Mono.empty());

        StepVerifier.create(userService.login(USER.getUsername(), PASSWORD))
            .expectErrorMessage("Username not found")
            .verify();

        verify(userRepository).findByUsername(USER.getUsername());
        verifyNoInteractions(encoder);
        verifyNoInteractions(jwtService);
    }
}
