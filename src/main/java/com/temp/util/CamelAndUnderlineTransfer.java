package com.temp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelAndUnderlineTransfer {

	public static void main(String[] args) {
		System.out.println(underline2Camel("THIRD_PARTY"));
	}
	
    public static String underline2Camel(String underline){
        Pattern pattern = Pattern.compile("[_]\\w");
        String camel = underline.toLowerCase();
        Matcher matcher = pattern.matcher(camel);
        while(matcher.find()) {
            String w = matcher.group().trim();
            camel = camel.replace(w,w.toUpperCase().replace("_", ""));
        }
        return camel;
    }
}
