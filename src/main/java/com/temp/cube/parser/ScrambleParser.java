package com.temp.cube.parser;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class ScrambleParser {

    public static List<SideTurnAction> parse(String scrambleStr) {
        List<SideTurnAction> result = Lists.newArrayList();
        List<String> tempList = Splitter.on(' ').splitToList(scrambleStr);
        for (String tempStr : tempList) {
            if (tempStr.length() == 1) {
                result.add(new SideTurnAction(SideTurnEnum.valueOf(tempStr), Direction.CLOCKWISE));
            } else {
                // 暂不处理2和'同时存在的情况
                SideTurnEnum sideTurnEnum = SideTurnEnum.valueOf(tempStr.substring(0, 1));
                if (tempStr.contains("2")) {
                    result.add(new SideTurnAction(sideTurnEnum, Direction.CLOCKWISE));
                    result.add(new SideTurnAction(sideTurnEnum, Direction.CLOCKWISE));
                } else {
                    result.add(new SideTurnAction(sideTurnEnum, Direction.COUNTERCLOCKWISE));
                }
            }
        }
        return result;
    }
}
