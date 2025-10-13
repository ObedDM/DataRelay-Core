package com.datarelay.core.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WsController {

    @MessageMapping("/feed")
    public String handle(String message) {
        return "Recieved: " + message;
    }
}