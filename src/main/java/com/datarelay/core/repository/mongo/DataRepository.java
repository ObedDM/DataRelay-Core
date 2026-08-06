package com.datarelay.core.repository.mongo;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;

import com.datarelay.core.entity.DataStream;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class DataRepository {
    
    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<DataStream> insertData(String collectionName, DataStream row) {
        return mongoTemplate.insert(row, collectionName);
    }

    public Flux<DataStream> getData(String collectionName) {
        return mongoTemplate.findAll(DataStream.class, collectionName);
    }
}
