package com.letschat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.letschat.entity.User;
import com.letschat.repository.ReactiveUserAccountRepository;

import reactor.core.publisher.Mono;

@Controller
public class UserLoginLogoutController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public ReactiveUserAccountRepository userRepository;
	
	@Autowired
	ChatController chatController;

	@GetMapping(path = "/login")
	public Mono<String> login() {

		return Mono.just("login");
	}
	
	@GetMapping(path = "/signup")
	public Mono<String> signup() {

		return Mono.just("login");
	}
	

	@PostMapping(path = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Mono<String> signup(@RequestBody MultiValueMap<String, String> map) {

		String username = map.get("regusername").get(0);
		String password = map.get("regpassword").get(0);
		String confirmPassword = map.get("repassword").get(0);
		if(!password.equals(confirmPassword)) {
			return Mono.error(new RuntimeException("Passwords do not match!!!"));
		}
		User user = new User(username, passwordEncoder.encode(password));
		userRepository.save(user).subscribe();
		
		
		
		chatController.getUserFluxSinkMap().values().
		forEach(sink -> sink.next(chatController.stringToSSE(username, "NEW_USER_EVENT")));
		
		return Mono.just("login");

	}
	
	


}
