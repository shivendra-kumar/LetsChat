package com.letschat.userMapping;

import java.util.HashSet;
import java.util.Set;

public class UserPair {

	private final Set<String> set;

 
    
    public UserPair(String a, String b) {
         set = new HashSet<String>();
         set.add(a);
         set.add(b);
    }

   
    public Set<String> getSet() {
		return set;
	}


    @Override
	public boolean equals(Object b) {
       return set.equals(((UserPair)b).getSet());
    }

    @Override
    public int hashCode() {
    	return set.hashCode();
    }
}