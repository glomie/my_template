package com.example.test;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

public class Test2 {

	public static void main(String[] args) {
		List<Data> list = Lists.newArrayList();
		Data data = new Data();
		data.setId(1);
		list.add(data);
		data.setId(2);
		list.add(data);
		System.out.println(list.get(0).getId());
	}
}

class Data {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
