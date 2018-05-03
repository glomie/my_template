package com.temp.designPatterns.mediator;

public class President implements Mediator {
    private Financial financial;
    private Market market;
    private Development development;

    @Override
    public void command(Department department) {
        if (department instanceof Market) {
            financial.selfAction();
        }else if (department instanceof Development) {
        	financial.selfAction();
        }
    }


	public Financial getFinancial() {
		return financial;
	}


	public void setFinancial(Financial financial) {
		this.financial = financial;
	}


	public Market getMarket() {
		return market;
	}


	public void setMarket(Market market) {
		this.market = market;
	}


	public Development getDevelopment() {
		return development;
	}


	public void setDevelopment(Development development) {
		this.development = development;
	}
    
    
}