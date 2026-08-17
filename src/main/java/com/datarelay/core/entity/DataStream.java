package com.datarelay.core.entity;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DataStream {
    
    @Id
    private UUID streamId;
    private UUID schemaId;

    private int ee;
    
    private Instant timestamp;
    private Map<String, Object> data;
}