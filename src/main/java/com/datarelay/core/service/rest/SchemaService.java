package com.datarelay.core.service.rest;

import java.util.List;
import java.util.UUID;

import com.datarelay.core.entity.Feature;
import com.datarelay.core.entity.DatasetSchema;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SchemaService {
    Flux<DatasetSchema> getUserSchemas();

    Mono<DatasetSchema> createSchema(DatasetSchema schema, List<Feature> features, UUID userId);

    Mono<DatasetSchema> updateSchema();

    Mono<DatasetSchema> deleteSchema();
}
