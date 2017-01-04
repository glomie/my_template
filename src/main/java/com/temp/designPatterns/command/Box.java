package com.temp.designPatterns.command;

public class Box {

	private Command openCommand;
	
	private Command resetCommand;

	public Command getOpenCommand() {
		return openCommand;
	}

	public void setOpenCommand(Command openCommand) {
		this.openCommand = openCommand;
	}

	public Command getResetCommand() {
		return resetCommand;
	}

	public void setResetCommand(Command resetCommand) {
		this.resetCommand = resetCommand;
	}
	
	public void openButtonPressed() {
		openCommand.execute();
	}
	
	public void resetButtionPressed() {
		resetCommand.execute();
	}
}
