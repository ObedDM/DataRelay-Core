package com.datarelay.core.service.rest;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.datarelay.core.entity.User;
import com.datarelay.core.repository.sql.UserRepository;
import com.datarelay.core.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Mono<User> createNewUser(String username, String password) {
        return userRepository.existsByUsername(username)
        .flatMap(exists -> {
            if (exists) {
                return Mono.error(new RuntimeException("Username already exists"));
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(encoder.encode(password));

            return userRepository.save(newUser);
        });
    }

    @Override
    public Mono<String> login(String username, String password) {
        return userRepository.findByUsername(username)
            .switchIfEmpty(Mono.error(new RuntimeException("Username not found")))
            .flatMap(user -> Mono.fromCallable(() -> encoder.matches(password, user.getPassword()))
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap(matches -> matches ? Mono.just(user) : Mono.error(new RuntimeException("Invalid credentials")))
            )
            .map(user -> {
                String token = jwtService.generateToken(user.getUserId().toString());

                ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", token)
                    .httpOnly(true)
                    .secure(false) // cambiar a true
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .build();

                /* return Mono.just(ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(token);
                */

                return token;
            });
    }
}