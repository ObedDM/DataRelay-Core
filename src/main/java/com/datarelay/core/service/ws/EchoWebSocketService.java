package com.datarelay.core.service.ws;

import reactor.core.publisher.Mono;

public interface EchoWebSocketService {
    public Mono<String> processEcho(String input);
}
