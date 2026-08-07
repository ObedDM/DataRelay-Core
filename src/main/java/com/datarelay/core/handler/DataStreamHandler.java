package com.datarelay.core.handler;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.datarelay.core.entity.DataStream;
import com.datarelay.core.service.ws.DataStreamService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataStreamHandler implements WebSocketHandler{
    private final DataStreamService DataStreamService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Flux<WebSocketMessage> response = session.receive()
            .map(msg -> msg.getPayloadAsText())
            .flatMap(text -> {
                DataStream data = objectMapper.readValue(text, new TypeReference<DataStream>() {});
                UUID schemaId = data.getSchemaId();

                return DataStreamService.addStreamingData(schemaId.toString(), data)
                    .map(savedData -> session.textMessage("data packet " + savedData.getStreamId() + " received"))
                    .onErrorResume(error -> {
                        log.error("Service error on {}: {}", session.getId(), error.getMessage());
                        return Mono.just(session.textMessage("could not add packet into collection"));
                    });
            })
            .doOnTerminate(() -> {
                log.info("Session closed: {}", session.getId());
            });

        return session.send(response);
    }
}