package com.datarelay.core.handler;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.datarelay.core.entity.DataStream;
import com.datarelay.core.helper.DynamicValidator;
import com.datarelay.core.repository.sql.FeatureRepository;
import com.datarelay.core.service.ws.DataStreamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataStreamHandler implements WebSocketHandler{
    private final DataStreamService dataStreamService;
    private final FeatureRepository featureRepository;
    private final DynamicValidator dynamicValidator;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> response = session.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .flatMap(text -> processMessage(text, session))
            .doOnTerminate(() -> log.info("Session closed: {}", session.getId()));

        return session.send(response);
    }

    private Mono<WebSocketMessage> processMessage(String text, WebSocketSession session) {

    return Mono.fromCallable(() -> objectMapper.readValue(text, DataStream.class))
        .flatMap((DataStream data) -> featureRepository.findBySchemaId(data.getSchemaId())
            .collectList()
            .flatMap(features -> Mono.fromCallable(() -> {
                    dynamicValidator.validateDataStream(data.getData(), features);
                    return data;
                })
            )
        )
        .flatMap((DataStream validData) -> dataStreamService.addStreamingData(
                validData.getSchemaId().toString(),
                validData
            )
        )
        .map(savedData -> session.textMessage("data packet " + savedData.getStreamId() + " received"))
        .onErrorResume(error -> {
            log.error("Service error on {}: {}", session.getId(),error.getMessage());
            return Mono.just(session.textMessage("could not add packet into collection"));
        });
    }
}