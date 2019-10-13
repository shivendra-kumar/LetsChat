package com.letschat.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

import com.letschat.session.LoggedInUsersService;

import reactor.core.publisher.Mono;

public class CustomLoginSuccessHandler extends RedirectServerAuthenticationSuccessHandler{


	
	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
		Authentication authentication) {
		LoggedInUsersService.getLoggedInUserMap().put(authentication.getName(),Boolean.TRUE);
		return super.onAuthenticationSuccess(webFilterExchange, authentication);
	}

	
}
