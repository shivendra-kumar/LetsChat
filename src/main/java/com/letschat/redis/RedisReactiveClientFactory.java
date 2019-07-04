package com.letschat.redis;

import javax.annotation.PostConstruct;

import org.redisson.Redisson;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

@Component
public class RedisReactiveClientFactory {
	
	private static RedissonReactiveClient client;
	
	@PostConstruct
	public void init() {
	/*	 Config conf = new Config();
		 conf.useSingleServer().setAddress("redis://10.0.50.128:6222") 
		       .setPassword("shivendra")
		       .setDatabase(7);
		 client = Redisson.createReactive(conf);*/
		
	}

	public static RedissonReactiveClient getClient() {
		return client;
	}

	

}
