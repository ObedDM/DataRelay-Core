package com.datarelay.core.repository.sql;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.datarelay.core.entity.DatasetSchema;

import reactor.core.publisher.Mono;

@Repository
public interface SchemaRepository extends R2dbcRepository<DatasetSchema, UUID> {
    Mono<DatasetSchema> findByName(String name);
    Mono<Boolean> existsByUserIdAndName(UUID userId, String name);
}
