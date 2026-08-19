package com.datarelay.core.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.dto.SchemaDTO;
import com.datarelay.core.entity.DatasetSchema;
import com.datarelay.core.mapper.SchemaMapper;
import com.datarelay.core.security.JwtService;
import com.datarelay.core.service.rest.SchemaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/schema")
@RequiredArgsConstructor
public class SchemaController {
    private final SchemaService schemaService;
    private final JwtService jwtService;
    private final SchemaMapper schemaMapper;

    @PostMapping("/create")
    public Mono<ResponseEntity<Object>> createSchema(@Valid @RequestBody SchemaDTO schema, @CookieValue("AUTH-TOKEN") String token) {
        UUID userId = UUID.fromString(jwtService.extractId(token));
        DatasetSchema newSchema = schemaMapper.toEntity(schema.schema());

        return schemaService.createSchema(newSchema, userId)
            .map(savedSchema -> {
                return ResponseEntity.status(HttpStatus.CREATED).body((Object) savedSchema);
            })
            .onErrorResume(error -> {
                log.error("Service error on createSchema: {}", error.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not create schema"));
            });
    }
}
