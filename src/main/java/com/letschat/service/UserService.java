package com.letschat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import com.letschat.entity.User;
import com.letschat.repository.ReactiveUserAccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    public ReactiveUserAccountRepository reactiveUserAccountRepository;
    
   

    public Mono<User> getCurrentUser() {
    	
    	 ReactiveSecurityContextHolder.getContext()
    	            .map(SecurityContext::getAuthentication)
    	            .subscribe(user->System.out.println("Principal Name ----->" +user.getName()));
    	         /*   .map(Authentication::getPrincipal).subscribe(System.out::println);*/
    	            
    	return reactiveUserAccountRepository.findByUsername("aryan").cast(User.class);
    	
    	            
    	            
    	
    }
  
}
