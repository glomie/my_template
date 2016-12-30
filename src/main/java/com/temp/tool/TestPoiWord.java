package com.temp.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class TestPoiWord {
	public static void main(String[] args) throws Exception {
		InputStream in = TestPoiWord.class.getClassLoader().
				getResourceAsStream("contract/test.doc");
		HWPFDocument doc = new HWPFDocument(in);
		Range range = doc.getRange();
		range.replaceText("{0}", "詹皇");
		File file = new File("C:/Intel/");
		if(!file.exists()) file.mkdirs();
		FileOutputStream fos = new FileOutputStream(new File("C:/Intel/me.doc"));
		doc.write(fos);
		fos.close();
		in.close();
		System.out.println("操作成功！");
	}
}
