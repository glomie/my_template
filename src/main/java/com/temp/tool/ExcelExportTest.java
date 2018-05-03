package com.temp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelExportTest {

	public static void main(String[] args) throws IOException {
		File file = new File("C:\\Users\\junyan\\Desktop\\新建文本文档 (19).txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		List<Map<String, Object>> listB = new ArrayList<>();
		while((line = br.readLine()) != null) {
			if(line.isEmpty()) continue;
			String[] arr = line.split("\t");
			String node = null;
			for(SmsSendNode smsSendNode : SmsSendNode.values()) {
				if(smsSendNode.name().equals(arr[0])) {
					node = smsSendNode.getValue();
					break;
				}
			}
			String channelName = arr[1].isEmpty() ? "--" : arr[1];
			String productName = arr[2].isEmpty() ? "--" : arr[2];
			String status = arr[3];
			String templateName = arr[4];
			String templateContent = arr[5];
			String platformCode = arr[6].equals("fed_sy_sms") ? "蜂e贷" : "口袋钱包";
			String templateType = arr[7];
			
			 Map<String,Object> map = new HashMap<>();
	         map.put("node",node);
	         map.put("channelName", channelName);
	         map.put("productName",productName);
	         map.put("status", status);
	         map.put("templateName", templateName);
	         map.put("templateContent", templateContent);
	         map.put("platformCode", platformCode);
	         map.put("templateType", templateType);
	         listB.add(map);
		}
		List<String> listName = new ArrayList<>();
        listName.add("触发节点");
        listName.add("渠道");
        listName.add("子产品");
        listName.add("状态");
        listName.add("短信模板");
        listName.add("平台签名");
        listName.add("模板分类");
        listName.add("模板内容");
        
        List<String> listId = new ArrayList<>();
        listId.add("node");
        listId.add("channelName");
        listId.add("productName");
        listId.add("status");
        listId.add("templateName");
        listId.add("platformCode");
        listId.add("templateType");
        listId.add("templateContent");
        
        
        ExportMapExcel exportExcelUtil = new ExportMapExcel();
        exportExcelUtil.exportExcel("短信导出",listName,listId,listB);
		br.close();
	}
}
