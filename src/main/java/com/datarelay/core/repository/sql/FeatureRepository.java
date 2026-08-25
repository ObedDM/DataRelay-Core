package com.datarelay.core.repository.sql;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.datarelay.core.entity.Feature;

import reactor.core.publisher.Flux;

@Repository
public interface FeatureRepository extends R2dbcRepository<Feature, UUID> {
    Flux<Feature> findBySchemaId(UUID schemaId);
}
