package com.temp.designPatterns.memento.flow;

public class Client {

	public static void main(String[] args) {
		FlowAMock mock = new FlowAMock("TestFlow");
		mock.runPhaseOne();
		FlowAMementoCareTaker careTaker = new FlowAMementoCareTaker();
		FlowAMockMemento memento = mock.createMemento();
		careTaker.saveMemento(memento);
		
		mock.schema1();
		mock.backToMemento(careTaker.retriveMemento());
		mock.schema2();
	}
}

