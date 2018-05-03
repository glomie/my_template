package com.temp.designPatterns.mediator;

public class Client {

	public static void main(String[] args) {
		President mediator = new President();
		
		Market market = new Market(mediator);
		Development development = new Development(mediator);
		Financial financial = new Financial(mediator);
		
		mediator.setMarket(market);
		mediator.setDevelopment(development);
		mediator.setFinancial(financial);
		
		development.selfAction();
		development.outAction();
	}
}
