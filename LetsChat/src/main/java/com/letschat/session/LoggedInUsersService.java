package com.letschat.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggedInUsersService {
	
	public static Map<String,Boolean> loggedInUserMap = new ConcurrentHashMap<>();
	
	public static Map<String,Boolean> getLoggedInUserMap() {
		return loggedInUserMap;
	}

}
