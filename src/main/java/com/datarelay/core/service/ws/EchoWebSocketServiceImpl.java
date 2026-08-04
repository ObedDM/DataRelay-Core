package com.datarelay.core.service.ws;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EchoWebSocketServiceImpl implements EchoWebSocketService {

    @Override
    public Mono<String> processEcho(String input) {      
        return Mono.just("Echo " + input);
    }
}
