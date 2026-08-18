package com.datarelay.core.service.rest;

import java.util.UUID;

import com.datarelay.core.entity.DatasetSchema;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SchemaService {
    Flux<DatasetSchema> getUserSchemas();

    Mono<DatasetSchema> createSchema(DatasetSchema schema, UUID userId);

    Mono<DatasetSchema> updateSchema();

    Mono<DatasetSchema> deleteSchema();
}
