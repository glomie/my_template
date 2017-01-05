package com.temp.designPatterns.command.calc;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

	private Command addCmd;
	
	private Command subCmd;
	
	private List<Command> historyCmds = new ArrayList<>();

	public Command getAddCmd() {
		return addCmd;
	}

	public void setAddCmd(Command addCmd) {
		this.addCmd = addCmd;
	}

	public Command getSubCmd() {
		return subCmd;
	}

	public void setSubCmd(Command subCmd) {
		this.subCmd = subCmd;
	}
	
	public void addPressed() {
		this.addCmd.execute();
		historyCmds.add(addCmd);
	}
	
	public void substractPressed() {
		this.subCmd.execute();
		historyCmds.add(subCmd);
	}
	
	public void undoPressed() {
		if(historyCmds.isEmpty()) {
			System.err.println("没有可撤销的命令");
		}else {
			Command cmd = historyCmds.get(historyCmds.size() - 1);
			cmd.undo();
			historyCmds.remove(cmd);
		}
	}
}
