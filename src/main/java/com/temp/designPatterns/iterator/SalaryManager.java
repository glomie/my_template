package com.temp.designPatterns.iterator;

public class SalaryManager {

	private PayModel[] pms;
	
	public PayModel[] getPays() {
		return this.pms;
	}
	
	public void calcSalary() {
		PayModel pm1 = new PayModel();
		pm1.setUserName("王五");
		pm1.setPay(2200);
		PayModel pm2 = new PayModel();
		pm2.setUserName("赵六");
		pm2.setPay(3600);
		
		pms = new PayModel[2];
		pms[0] = pm1;
		pms[1] = pm2;
	}
}
