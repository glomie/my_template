package com.temp.designPatterns.iterator;

import java.util.ArrayList;
import java.util.List;

public class PayManager {

	private List<PayModel> list = new ArrayList<PayModel>();
	
	public List<PayModel> getPayList() {
		return this.list;
	}
	
	public void calcPay() {
		PayModel pm1 = new PayModel();
		pm1.setUserName("张三");
		pm1.setPay(3000);
		PayModel pm2 = new PayModel();
		pm2.setUserName("李四");
		pm2.setPay(4000);
		list.add(pm1);
		list.add(pm2);
	}
}
