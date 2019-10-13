package com.letschat.session;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

@Configuration
//@EnableRedissonWebSession
@EnableSpringWebSession
public class SpringSessionConfig {
  
	
	@Bean
	public ReactiveSessionRepository reactiveSessionRepository() {
		return new ReactiveMapSessionRepository(new ConcurrentHashMap<>()); 
	}

}