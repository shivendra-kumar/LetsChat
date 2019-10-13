package com.letschat.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.letschat.repository.ReactiveMessageRepository;
import com.letschat.repository.ReactiveUserAccountRepository;



@SpringBootApplication
@EnableReactiveMongoRepositories({"com.letschat.repository"})
@ComponentScan("com.letschat")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	  PasswordEncoder passwordEncoder() {
	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	  }

	  /*@Bean
	  CommandLineRunner start(ReactiveUserAccountRepository userRepository,ReactiveMessageRepository messageRepository){
	    return args -> {
	    	userRepository.deleteAll().log("Deleting user ---> ").subscribe();
	    	messageRepository.deleteAll().subscribe();
	    };
	  }*/
}