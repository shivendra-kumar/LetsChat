package com.letschat.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.letschat.entity.Message;
import com.letschat.entity.User;
import com.letschat.repository.ReactiveMessageRepository;
import com.letschat.repository.ReactiveUserAccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/react")
public class ChatController {

	private Map<String, FluxSink<ServerSentEvent<String>>> userFluxSinkMap = new ConcurrentHashMap<>();

	public Map<String, FluxSink<ServerSentEvent<String>>> getUserFluxSinkMap() {
		return userFluxSinkMap;
	}


	@Autowired
	private ReactiveUserAccountRepository userRepository;

	@Autowired
	private ReactiveMessageRepository messageRepository;

	@PostMapping(path = "/sendMessage")
	public void sendMessage(@RequestBody String message, @AuthenticationPrincipal UserDetails userDetails)
			throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		String currentuserName = userDetails.getUsername();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> argumentMap = mapper.readValue(message, Map.class);
		String username = argumentMap.get("username");
		String msg = argumentMap.get("msgtxt");

		Instant now = Instant.now();
		ZoneId zoneId = ZoneId.of("Asia/Kolkata");
		ZonedDateTime dateAndTime = ZonedDateTime.ofInstant(now, zoneId);

		Message messageObject = new Message(username, currentuserName, msg, dateAndTime.toEpochSecond());

		userFluxSinkMap.get(currentuserName).next(stringToSSE(objectToJson(messageObject), "MESSAGE_EVENT"));
		FluxSink<ServerSentEvent<String>> sink = userFluxSinkMap.get(username);

		if (null != sink) {
			if (sink.isCancelled()) {
				userFluxSinkMap.remove(username);
			}

			sink.next(stringToSSE(objectToJson(messageObject), "MESSAGE_EVENT"));

		}

		this.saveMessage(messageObject).subscribe();

	}

	@GetMapping(path = "/recieveMessage", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<String>> recieveMessage(@AuthenticationPrincipal UserDetails userDetails) {
		
		String username = userDetails.getUsername();
		return Flux.create(emitter -> userFluxSinkMap.put(username, emitter));

	}

	@GetMapping(path = "/populateUsers")
	public Mono<List<String>> populateUsersInChatWindow(@AuthenticationPrincipal UserDetails userDetails) {
		return userRepository.findAll().map(User::getUsername).filter(name -> !name.equals(userDetails.getUsername()))
				.collect(Collectors.toList());
	}

	@PostMapping(path = "/populateChat")
	public void populateUserChat(@RequestBody String sender,
			@AuthenticationPrincipal UserDetails userDetails) {

		String currentUserName = userDetails.getUsername();
		Flux<Message> recievedMessagesFlux =  messageRepository.findAllBySenderAndRecepient(sender, currentUserName);
		Flux<Message> sentMessagesFlux = messageRepository.findAllBySenderAndRecepient(currentUserName, sender);
		Flux<Message> mergedFlux = Flux.merge(recievedMessagesFlux, sentMessagesFlux);
		FluxSink<ServerSentEvent<String>> sink = userFluxSinkMap.get(currentUserName);
		
		
		
		mergedFlux.map(message -> sink.next(stringToSSE(objectToJson(message), "MESSAGE_EVENT"))).subscribe();
		
		
	

	}
	
	public String objectToJson(Object obj)  {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ServerSentEvent<String> stringToSSE(String data,String event){
		return ServerSentEvent.<String>builder().event(event).data(data).build();
	}
	
	
	@GetMapping(path = "/getCurrentUser")
	public Mono<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
		return Mono.just(userDetails.getUsername());
	}
	

	private Mono<Message> saveMessage(Message message) {

		return messageRepository.save(message);
	}

}
