package com.letschat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.letschat.entity.Message;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveMessageRepository extends ReactiveMongoRepository<Message, String>{

	Flux<Message> findAllBySenderAndRecepient(Mono<String> sender,Mono<String> recepient);
	
	
}
