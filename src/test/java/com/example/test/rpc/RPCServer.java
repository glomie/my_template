package com.example.test.rpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class RPCServer {

	private static final ExecutorService taskPool = Executors.newFixedThreadPool(50);
	
	private static final ConcurrentHashMap<String, Object> serviceTargets = new ConcurrentHashMap<>();
	
	private static AtomicBoolean run = new AtomicBoolean(false);
	
	public void registerService(Object service) {
		Class<?>[] interfaces = service.getClass().getInterfaces();
		if(interfaces == null) {
			throw new IllegalArgumentException("服务对象必须实现接口");
		}
		Class<?> interfacez = interfaces[0];
		String interfaceName = interfacez.getName();
		serviceTargets.put(interfaceName, service);
	}
}
