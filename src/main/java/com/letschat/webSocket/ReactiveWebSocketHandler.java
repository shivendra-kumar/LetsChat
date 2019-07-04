package com.letschat.webSocket;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
  
 
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession
        		.send(Flux.interval(Duration.ofSeconds(5)).map(l -> "From Server!!!")
          .map(webSocketSession::textMessage))
          .and(webSocketSession.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .log());
    }
}