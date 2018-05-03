package com.temp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExportTest2 {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\junyan\\Desktop\\短信模板.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		List<Map<String, Object>> listB = new ArrayList<>();
		while((line = br.readLine()) != null) {
			if(line.isEmpty()) continue;
			String[] arr = line.split("\t");
			
			
			String platformCode = arr[0].equals("fed_sy_sms") ? "蜂e贷" : "口袋钱包";
			String templateCode = arr[1];
			String templateType = arr[2];
			String templateName = arr[3];
			String templateContent = arr[4];
			
			 Map<String,Object> map = new HashMap<>();
	         map.put("platform",platformCode);
	         map.put("templateCode", templateCode);
	         map.put("templateType",templateType);
	         map.put("templateName", templateName);
	         map.put("templateContent", templateContent);
	         listB.add(map);
		}
		List<String> listName = new ArrayList<>();
        listName.add("短信平台");
        listName.add("模板编号");
        listName.add("模板分类");
        listName.add("短信模板");
        listName.add("模板内容");
        
        List<String> listId = new ArrayList<>();
        listId.add("platform");
        listId.add("templateCode");
        listId.add("templateType");
        listId.add("templateName");
        listId.add("templateContent");
        
        
        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcel("短信导出",listName,listId,listB);
		br.close();
	}
}
