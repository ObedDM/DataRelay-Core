package com.datarelay.core.service.ws;

import com.datarelay.core.entity.DataStream;

import reactor.core.publisher.Mono;

public interface DataStreamService {
    public Mono<DataStream> addStreamingData(String collectionName, DataStream row);
}
