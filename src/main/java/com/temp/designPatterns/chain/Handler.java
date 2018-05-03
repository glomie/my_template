package com.temp.designPatterns.chain;

public interface Handler {

	void handleLeaveNote(LeaveNote leaveNote);
	
	void setNextHandler(Handler h);
}
