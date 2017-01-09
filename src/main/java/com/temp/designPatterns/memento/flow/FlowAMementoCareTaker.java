package com.temp.designPatterns.memento.flow;

public class FlowAMementoCareTaker {

	private FlowAMockMemento memento;
	
	/**
	 * 保存备忘录
	 * @author junyan
	 * @date 2017年1月9日 上午11:39:03
	 * @param memento
	 */
	public void saveMemento(FlowAMockMemento memento) {
		this.memento = memento;
	}
	
	/**
	 * 取出备忘录
	 * @author junyan
	 * @date 2017年1月9日 上午11:39:18
	 * @return
	 */
	public FlowAMockMemento retriveMemento() {
		return this.memento;
	}
}
