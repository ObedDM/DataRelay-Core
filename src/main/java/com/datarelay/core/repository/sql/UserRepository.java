package com.datarelay.core.repository.sql;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.datarelay.core.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, UUID> {
    Mono<User> findByUsername(String username);
}