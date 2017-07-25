package com.temp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AuthorityRepeatData {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\hzwjy\\Desktop\\新建文本文档.txt")));
		String line = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while((line = br.readLine()) != null) {
			if(line.isEmpty()) continue;
			String[] tmp = line.split("\t");
			String roleId = tmp[1];
			String menuId = tmp[2];
			System.out.println("update role_privilege set del_flag = '1' where role_id = '" + roleId + "' and " + "menu_id = '" + menuId + "';");
			System.out.printf("insert into role_privilege(id,role_id,menu_id,del_flag,create_date,update_date) values('%s','%s','%s','%s','%s','%s');\n",
					UUID.randomUUID().toString().replaceAll("-", ""),
					roleId,menuId,"0",sdf.format(new Date()),sdf.format(new Date()));
		}
		br.close();
	}
}
