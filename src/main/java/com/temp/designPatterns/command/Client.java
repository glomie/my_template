package com.temp.designPatterns.command;

public class Client {

	public static void main(String[] args) {
		MainboardApi mainboardApi = new GigaMainboard();
		OpenCommand command = new OpenCommand(mainboardApi);
		Box box = new Box();
		box.setOpenCommand(command);
		box.setResetCommand(new ResetCommand(new MsiMainboard()));
		box.openButtonPressed();
		box.resetButtionPressed();
	}
}
