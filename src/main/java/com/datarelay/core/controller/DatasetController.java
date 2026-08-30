package com.datarelay.core.controller;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datarelay.core.repository.sql.SchemaRepository;
import com.datarelay.core.security.JwtService;
import com.datarelay.core.service.rest.DatasetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DatasetController {
    private final DatasetService datasetService;
    private final SchemaRepository schemaRepository;
    private final JwtService jwtService;

    @GetMapping(value = "/getDataset/{name}", produces = "text/csv")
    public Mono<ResponseEntity<Flux<DataBuffer>>> exportDataset(@PathVariable String name, @CookieValue("AUTH-TOKEN") String token, ServerHttpResponse response) {
        UUID userId = UUID.fromString(jwtService.extractId(token));
        
        return schemaRepository.findSchemaIdByNameAndUserId(name, userId)
            .map(schemaId -> {
                log.info("dataset found: " + schemaId.toString());
                String filename = String.format("%s.csv", name);
                Flux<DataBuffer> dataStream = datasetService.streamDataset(schemaId, response.bufferFactory());

                return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(dataStream);
            })
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
            .onErrorResume(error -> {
                log.error("error exporting dataset: {}", error.getMessage());
                DataBuffer errorBuffer = response.bufferFactory().wrap("Could not export dataset".getBytes(StandardCharsets.UTF_8));

                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Flux.just(errorBuffer)));
            });
    }
}
