package com.datarelay.core.service.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.datarelay.core.entity.Feature;
import com.datarelay.core.entity.DatasetSchema;
import com.datarelay.core.repository.sql.FeatureRepository;
import com.datarelay.core.repository.sql.SchemaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchemaServiceImpl implements SchemaService {
    private final SchemaRepository schemaRepository;
    private final FeatureRepository featureRepository;
    
    @Override
    public Flux<DatasetSchema> getUserSchemas() {
        return Flux.empty();
    }

    @Override
    public Mono<DatasetSchema> createSchema(DatasetSchema schema, List<Feature> features, UUID userId) {
        schema.setUserId(userId);
        
        return schemaRepository.save(schema)
            .flatMap(savedSchema -> {
                for (Feature feature : features) {
                    feature.setSchemaId(savedSchema.getSchemaId());
                }

                return featureRepository.saveAll(features).collectList()
                    .thenReturn(savedSchema);
            });
    }

    @Override
    public Mono<DatasetSchema> updateSchema() {
        return Mono.empty();
    }

    @Override
    public Mono<DatasetSchema> deleteSchema() {
        return Mono.empty();
    }
}