package com.letschat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class MainController {

	
	
    @GetMapping("/")
    public Mono<String> index(final Model model) {
        return Mono.just("index");
    }
    
   

}
