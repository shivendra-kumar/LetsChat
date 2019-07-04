package com.letschat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.letschat.entity.Message;

import reactor.core.publisher.Flux;

@Repository
public interface ReactiveMessageRepository extends ReactiveMongoRepository<Message, String>{

	Flux<Message> findAllBySenderAndRecepient(String sender,String recepient);
	
	
}
