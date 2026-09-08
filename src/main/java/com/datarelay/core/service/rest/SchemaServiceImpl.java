package com.datarelay.core.service.rest;

import java.sql.Savepoint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datarelay.core.entity.Feature;
import com.datarelay.core.entity.DatasetSchema;
import com.datarelay.core.entity.Dimension;
import com.datarelay.core.repository.sql.DimensioneRepository;
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
    private final DimensioneRepository dimensionRepository;
    
    @Override
    public Flux<DatasetSchema> getUserSchemas() {
        return Flux.empty();
    }

    @Override
    @Transactional
    public Mono<DatasetSchema> createSchema(DatasetSchema schema, List<Feature> features, List<Dimension> dimensions, UUID userId) {
        schema.setUserId(userId);

        if (dimensions.isEmpty())
            schema.setHasIndex(true);

        else
            schema.setHasIndex(false);
        System.out.println(dimensions);
        
        
        return schemaRepository.save(schema)
            .flatMap(savedSchema -> {
                Mono<Void> saveDimensions = Mono.empty();
                Mono<Void> saveFeatures = Mono.empty();

                for (Feature feature : features) {
                    feature.setSchemaId(savedSchema.getSchemaId());
                    System.out.println(feature);
                }

                if (!dimensions.isEmpty()) {
                    for (Dimension dimension : dimensions) {
                        dimension.setSchemaId(savedSchema.getSchemaId());
                        System.out.println(dimension);
                    }

                    saveDimensions = dimensionRepository.saveAll(dimensions).then();
                }

                saveFeatures = featureRepository.saveAll(features).then();

                return Mono.when(saveDimensions, saveFeatures)
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