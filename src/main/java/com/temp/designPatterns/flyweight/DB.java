package com.temp.designPatterns.flyweight;

import java.util.ArrayList;
import java.util.Collection;

public class DB {

	public static Collection<String> colDB = new ArrayList<String>();
	
	static {
		colDB.add("张三,人员列表,查看");
		colDB.add("李四,人员列表,查看");
		colDB.add("李四,薪资数据,查看");
		colDB.add("李四,薪资数据,修改");
		
		for(int i = 0 ; i < 3; i++) {
			colDB.add("张三" + i + ",人员列表,查看");
		}
	}
}
