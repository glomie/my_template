package com.temp.designPatterns.mediator;

public class Development extends Department {

	public Development(Mediator mediator) {
		super(mediator);
	}

	@Override
	void selfAction() {
		System.out.println("专心科研，开发项目！");
	}

	@Override
	void outAction() {
		System.out.println("汇报工作！没钱了，需要资金支持！");
		getMediator().command(this);
	}

}
