package com.letschat.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.letschat.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUserAccountRepository extends ReactiveMongoRepository<User, String>{
	
	Mono<UserDetails> findByUsername(String username);
}
