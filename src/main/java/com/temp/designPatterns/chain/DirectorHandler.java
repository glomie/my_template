package com.temp.designPatterns.chain;

public class DirectorHandler implements Handler {
	
	private Handler nextHandler;

	@Override
	public void handleLeaveNote(LeaveNote leaveNote) {
		if(leaveNote.getDayNum() <= 3) {
			System.out.println("主管同意" + leaveNote.getName() + "申请请假" + leaveNote.getDayNum() + "天,原因:" + leaveNote.getReason());
		}else {
			nextHandler.handleLeaveNote(leaveNote);
		}
	}

	@Override
	public void setNextHandler(Handler h) {
		this.nextHandler = h;
	}

}
