package com.datarelay.core.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.datarelay.core.service.ws.EchoWebSocketService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class EchoHandler implements WebSocketHandler {
    private final EchoWebSocketService echoService;

    public EchoHandler(EchoWebSocketService echoService) {
        this.echoService = echoService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(
            session.receive()
            .map(msg -> msg.getPayloadAsText())
            .flatMap(text -> echoService.processEcho(text)
            .doOnError(error -> {
                    log.error("Service error on {}: {}", session.getId(), error.getMessage());
                })
            )
            .map(response -> session.textMessage(response))
        )
        .doOnTerminate(() -> {
            // session ends or drops
            log.info("Session closed: {}", session.getId());
        });
    }
}
