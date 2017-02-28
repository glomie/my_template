package com.temp.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ImportMenusIntoDB {

	public static void main(String[] args) {
		try {
			File sqlFile = new File("E:/menu.sql");
			File sql2File = new File("E:/role.sql");
			roleOs = new FileOutputStream(sql2File);
			FileOutputStream outputStream = new FileOutputStream(sqlFile);
			(new ImportMenusIntoDB()).readFromExcel("E:/menu.xlsx", outputStream, false);
			outputStream.close();
			roleOs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readFromExcel(String filePath,OutputStream outputStream, boolean isPrint) throws Exception {
		File file = new File(filePath);
		if(!file.exists()) return ;
		Workbook workbook = WorkbookFactory.create(file);
		Sheet sheet = workbook.getSheetAt(0);
		String[] parentIds = new String[6];
		parentIds[0] = "-1";
		String[] displayNames = new String[6];
		
		for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row row = sheet.getRow(rowNum);
			if(row == null) continue;
			for(int cellNum = 0; cellNum < (int)row.getLastCellNum(); cellNum++) {
				Cell cell = row.getCell(cellNum);
				if(cell != null) {
					String value = cell.getStringCellValue();
					if(StringUtils.isBlank(value)) continue;
					parentIds[cellNum + 1] = this.uuid();
					displayNames[cellNum] = value;
					String sql = this.printSql(parentIds, cellNum + 1, displayNames, isPrint) + '\n';
					outputStream.write(sql.getBytes());
				}
			}
			
		}
	}
	
	
	private int count = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String printSql(String[] parentIds, int len, String[] displayNames, boolean isPrint) {
		String name = this.getName(displayNames, len);
		String nowDate = sdf.format(new Date());
		String sql = "insert into menu_resource(id,name,display_name,remarks,ranks,parent_id,create_date,update_date) values (";
    	String out = String.format(sql + "'%s','%s','%s','%s','%s','%s','%s','%s');", 
    			parentIds[len], 
    			name, 
    			displayNames[len - 1], 
    			String.format("%03d", count++), 
    			String.valueOf(len - 1), 
    			parentIds[len - 1], 
    			nowDate, nowDate);
    	
    	if(isPrint) System.out.println(out);
    	this.writeRole(parentIds[len]);
    	return out;
	}
	
	private String uuid() {
    	return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
	
	private String getName(String[] alias, int len) {
		boolean flag = true;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < len; i++) {
			if(!flag) {
				sb.append("-");
			}
			sb.append(getPinYin(alias[i]));
			flag = false;
		}
		
		return sb.toString();
	}
	
	private String getPinYin(String inputString) {  
        
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
  
        char[] input = inputString.trim().toCharArray();  
        StringBuffer output = new StringBuffer();  
  
        try {  
            for (int i = 0; i < input.length; i++) {  
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);  
                    output.append(temp[0].substring(0, 1));
                } else { 
                    output.append(Character.toString(input[i]));  
                }
            }  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
        return output.toString().toLowerCase();  
    }
	
	private static OutputStream roleOs = null;
	
	private void writeRole(String menuId) {
		String sql = "insert into role_privilege(id, role_id, menu_id) values (";
		String out = String.format(sql + "'%s','%s','%s');", uuid(), "6b13d220fbbe494eb1d21cd6e4f903da", menuId);
		try {
			roleOs.write((out + '\n').getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
