package com.temp.designPatterns.chain;

public class ManagerHandler implements Handler {
	
	private Handler nextHandler;

	@Override
	public void handleLeaveNote(LeaveNote leaveNote) {
		System.out.println("总经理同意" + leaveNote.getName() + "申请请假" + leaveNote.getDayNum() + "天,原因:" + leaveNote.getReason());
	}

	@Override
	public void setNextHandler(Handler h) {
		this.nextHandler = h;
	}

}
