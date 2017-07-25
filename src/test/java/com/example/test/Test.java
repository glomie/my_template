package com.example.test;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;


/**
 * I just want to test it!
 * @fileName Test.java
 * @author junyan
 * @date 2016年7月28日 下午2:22:40
 */
public class Test {

	public static void main(String[] args) {
		System.out.println(StringUtils.leftPad(String.valueOf(1), 3, '0'));
		System.out.println(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		String name = "吴均";
		StringBuffer sb = new StringBuffer();
		sb.append(name.substring(0, 1));
		for(int i = 0 ; i < name.length() - 1; i++) 
			sb.append("*");
		
		System.out.println(sb.toString());
		try {
			int i = 1 / 0;
		}catch (Exception e) {
		}
		List<String> list = null;
		for(String str : list) {
			
		}
	}
}

class Model1 {
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
