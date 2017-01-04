package com.temp.designPatterns.command;

public class OpenCommand implements Command {
	
	private MainboardApi mainboardApi;
	
	public OpenCommand(MainboardApi mainboardApi) {
		this.mainboardApi = mainboardApi;
	}

	@Override
	public void execute() {
		mainboardApi.open();
	}

}
