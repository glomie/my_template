package com.temp.designPatterns.chain;

public class ProjectManager extends Handler {

	@Override
	public String handleFeeRequest(String user, double fee) {
		String str = null;
		if(fee < 500) {
			str = "项目经理处理这件事";
		}else {
			if(this.successor != null) {
				return successor.handleFeeRequest(user, fee);
			}
		}
		return str;
	}

}
