package com.temp.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import net.sourceforge.pinyin4j.PinyinHelper;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;  
  
public class TagGen {  
    
    public static String getPinYin(String inputString) {  
          
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
  
        char[] input = inputString.trim().toCharArray();  
        StringBuffer output = new StringBuffer("");  
  
        try {  
            for (int i = 0; i < input.length; i++) {  
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);  
                    output.append(temp[0].substring(0, 1));
                } else  
                    output.append(Character.toString(input[i]));  
            }  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
        return output.toString();  
    }  
      
    private static String uuid() {
    	return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static List<String> list = new ArrayList<String>();
    
    private static void printSql(String ... alias) {
    	String now = sdf.format(new Date());
    	StringBuffer py = new StringBuffer();
    	StringBuffer name = new StringBuffer();
    	boolean flag = true;
    	for(String str : alias) {
    		if(!flag) {
    			py.append("-");
    			name.append("-");
    		}
    		py.append(getPinYin(str));
    		name.append(str);
    		flag = false;
    	}
    	String sql = "INSERT INTO `trc_acl_db`.`access_tag_tb` (`id`, `access_tag`, `access_tag_alias`, `create_time`, `update_time`) VALUES (";
    	String out = String.format(sql + "'%s','%s','%s','%s','%s');", uuid(), py.toString(), name.toString(), now, now);
    	System.out.println(out);
    	list.add(out);
    }
    
    public static void main(String[] args) {  
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
        while(true) {
        	System.out.println("Input the the first class(Enter 'q' to exit):");
        	String first = scanner.nextLine();
        	if("q".equals(first)) break;
        	printSql(first);
        	while(true) {
        		System.out.println("Input the the second class(Enter 'q' to exit):");
        		String second = scanner.nextLine();
        		if("q".equals(second)) break;
        		printSql(first, second);
        		while(true) {
        			System.out.println("Input the the third class(Enter 'q' to exit):");
        			String third = scanner.nextLine();
        			if("q".equals(third)) break;
        			printSql(first, second, third);
        		}
        	}
        }
        
        System.out.println("All SQL:");
        for(String str : list) {
        	System.out.println(str);
        }
    }  
      
}  