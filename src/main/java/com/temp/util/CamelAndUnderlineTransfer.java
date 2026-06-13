package com.temp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelAndUnderlineTransfer {

	public static void main(String[] args) {
		System.out.println(underline2Camel("THIRD_PARTY"));   // thirdParty
		System.out.println(camel2Underline("thirdParty"));    // third_party
		System.out.println(camel2Underline("myURLParser"));   // my_url_parser
	}

    /** 下划线（含全大写）转小驼峰：THIRD_PARTY -> thirdParty */
    public static String underline2Camel(String underline) {
        if (underline == null || underline.isEmpty()) return underline;
        Pattern pattern = Pattern.compile("[_]\\w");
        String camel = underline.toLowerCase();
        Matcher matcher = pattern.matcher(camel);
        while (matcher.find()) {
            String w = matcher.group().trim();
            camel = camel.replace(w, w.toUpperCase().replace("_", ""));
        }
        return camel;
    }

    /** 驼峰转下划线小写：thirdParty -> third_party */
    public static String camel2Underline(String camel) {
        if (camel == null || camel.isEmpty()) return camel;
        // 在每个大写字母前插入下划线，然后全部转小写
        return camel.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                    .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
                    .toLowerCase();
    }
}
