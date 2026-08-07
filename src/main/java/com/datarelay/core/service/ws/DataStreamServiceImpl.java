package com.datarelay.core.service.ws;

import org.springframework.stereotype.Service;

import com.datarelay.core.entity.DataStream;
import com.datarelay.core.repository.mongo.DataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class DataStreamServiceImpl implements DataStreamService {
    private final DataRepository dataRepository;
    
    @Override
    public Mono<DataStream> addStreamingData(String collectionName, DataStream row) {
        return dataRepository.insertData(collectionName, row);
    }
}
