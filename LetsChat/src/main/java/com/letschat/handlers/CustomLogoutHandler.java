package com.letschat.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;

import com.letschat.session.LoggedInUsersService;

import reactor.core.publisher.Mono;

public class CustomLogoutHandler extends SecurityContextServerLogoutHandler{
	

	@Override
	public Mono<Void> logout(WebFilterExchange exchange,
		Authentication authentication) {
		
		LoggedInUsersService.getLoggedInUserMap().remove(authentication.getName());
		return super.logout(exchange, authentication);
	}
	
	

}
