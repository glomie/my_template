package com.temp.designPatterns.flyweight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecurityMgr {

	private SecurityMgr() {}
	
	private static class SecurityMgrHolder {
		private static final SecurityMgr instance = new SecurityMgr();
	}
	
	public static SecurityMgr getInstance() {
		return SecurityMgrHolder.instance;
	}
	
	private Map<String, Collection<Flyweight>> map = new HashMap<>();
	
	public void login(String user) {
		
	}
	
	private Collection<Flyweight> queryByUser(String user) {
		Collection<Flyweight> coll = new ArrayList<>();
		return coll;
	}
}
