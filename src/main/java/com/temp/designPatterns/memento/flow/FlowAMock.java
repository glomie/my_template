package com.temp.designPatterns.memento.flow;

public class FlowAMock {

	private String flowName;
	
	private int tempResult;
	
	private String tempState;
	
	public FlowAMock(String flowName) {
		this.setFlowName(flowName);
	}
	
	public void runPhaseOne() {
		this.tempResult = 3;
		this.tempState = "PhaseOne";
	}
	
	public void schema1() {
		this.tempState += ", Schema1";
		System.out.println(this.tempState + " : now run " + this.tempResult);
		this.tempResult = 11;
	}
	
	public void schema2() {
		this.tempState += ", Schema2";
		System.out.println(this.tempState + " : now run " + this.tempResult);
		this.tempResult = 22;
	}
	
	public FlowAMockMemento createMemento() {
		return new MementoImpl(this.tempResult, this.tempState);
	}
	
	public void backToMemento(FlowAMockMemento memento) {
		MementoImpl mementoImpl = (MementoImpl) memento;
		this.tempResult = mementoImpl.getTempResult();
		this.tempState = mementoImpl.getTempState();
	}
	
	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	private static class MementoImpl implements FlowAMockMemento {
		
		private int tempResult;
		
		private String tempState;
		
		public MementoImpl(int tempResult, String tempState) {
			this.tempResult = tempResult;
			this.tempState = tempState;
		}

		public int getTempResult() {
			return tempResult;
		}

		public String getTempState() {
			return tempState;
		}
		
	}
}
