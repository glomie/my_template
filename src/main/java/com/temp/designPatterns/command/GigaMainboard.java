package com.temp.designPatterns.command;

public class GigaMainboard implements MainboardApi {

	@Override
	public void open() {
		System.out.println("技嘉主板正在启动。。。");
	}

	@Override
	public void reset() {
		System.out.println("技嘉主板正在重启。。。");
	}

}
