package com.temp.designPatterns.chain;

public class DepManager extends Handler {

	@Override
	public String handleFeeRequest(String user, double fee) {
		String str = null;
		if(fee < 1000) {
			str = "部门经理处理这件事";
		}else {
			return this.successor.handleFeeRequest(user, fee);
		}
		return str;
	}

}
