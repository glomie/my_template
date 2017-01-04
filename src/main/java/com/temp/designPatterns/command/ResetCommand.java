package com.temp.designPatterns.command;

public class ResetCommand implements Command {

	private MainboardApi mainboardApi;
	
	public ResetCommand(MainboardApi mainboardApi) {
		this.mainboardApi = mainboardApi;
	}

	@Override
	public void execute() {
		mainboardApi.reset();
	}
	
	
}
