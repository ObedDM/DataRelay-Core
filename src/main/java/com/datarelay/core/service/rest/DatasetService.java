package com.datarelay.core.service.rest;

import java.util.UUID;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

import reactor.core.publisher.Flux;

public interface DatasetService {
    Flux<DataBuffer> streamDataset(UUID schemaId, DataBufferFactory bufferFactory);
}