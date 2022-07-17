package com.temp.leetcode;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

public class InputUtils {

    public static int[] toArray(String str) {
        String substring = str.substring(0, str.length() - 1);
        if (StringUtils.isBlank(substring)) {
            return null;
        }
        return Splitter.on(Symbols.COMMA).omitEmptyStrings().trimResults().splitToList(substring).stream().mapToInt(Integer::parseInt).toArray();
    }


}
