package com.temp.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContrastAutority {

	public static void main(String[] args) throws IOException {
		String testApiResourceFilePath = "C:\\Users\\hzwjy\\Desktop\\test_api_resource.sql";
		String prdApiResourceFilePath = "C:\\Users\\hzwjy\\Desktop\\prd_api_resource.sql";
		File fileTest = new File(testApiResourceFilePath);
		File filePrd = new File(prdApiResourceFilePath);
		
		
		BufferedReader brPrd = new BufferedReader(new FileReader(filePrd));
		String line2 = null;
		Map<String, String> mapPrd = new HashMap<>();
		Set<String> setPrd = new HashSet<>();
		while((line2 = brPrd.readLine()) != null) {
			String[] tmp = line2.split("\'");
			String id = tmp[1];
			String api = tmp[5];
			mapPrd.put(api, id);
			setPrd.add(id);
		}
		brPrd.close();
		
		BufferedReader brTest = new BufferedReader(new FileReader(fileTest));
		String line1 = null;
		while((line1 = brTest.readLine()) != null) {
			String[] tmp = line1.split("\'");
			String id = tmp[1];
			String api = tmp[5];
			if(mapPrd.get(api) == null) {
				System.out.println("变动后的api=" + api + "，id是否两边都有" + setPrd.contains(id));
			}
		}
		brTest.close();
		
	}
}
