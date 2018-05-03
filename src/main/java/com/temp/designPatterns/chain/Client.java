package com.temp.designPatterns.chain;

public class Client {

	public static void main(String[] args) {
		LeaveNote leaveNote = new LeaveNote();
		leaveNote.setDayNum(4);
		leaveNote.setName("小明");
		leaveNote.setReason("肚子痛");
		
		Handler h1 = new DirectorHandler();
		Handler h2 = new ViceManagerHandler();
		Handler h3 = new ManagerHandler();
		h1.setNextHandler(h2);
		h2.setNextHandler(h3);
		
		h1.handleLeaveNote(leaveNote);
	}
}
