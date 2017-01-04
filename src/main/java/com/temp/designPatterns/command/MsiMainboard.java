package com.temp.designPatterns.command;

public class MsiMainboard implements MainboardApi {

	@Override
	public void open() {
		System.out.println("微星主板正在启动。。。");
	}

	@Override
	public void reset() {
		System.out.println("微星主板正在重启。。。");
	}

}
