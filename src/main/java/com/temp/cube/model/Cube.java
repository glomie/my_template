package com.temp.cube.model;

import com.temp.cube.constants.Direction;
import com.temp.cube.constants.SideTurnEnum;
import com.temp.cube.turn.TurnFactory;
import com.temp.cube.utils.OutputUtils;

/**
 * 整个立方体的抽象
 */
public class Cube {

    /**
     * 前面的一面，初始面
     */
    private Side frontSide;

    /**
     * 右边的一面, y转体后正对
     */
    private Side rightSide;

    /**
     * 后面的一面, y2转体后正对
     */
    private Side backSide;

    /**
     * 左边的一面, y'转体后正对
     */
    private Side leftSide;

    /**
     * 底下的一面, z转体后正对
     */
    private Side downSide;

    /**
     * 顶上的一面, z'转体后正对
     */
    private Side upSide;

    /**
     * 屏蔽隐藏默认构造
     */
    private Cube() {}

    /**
     * 初始化
     */
    public static Cube init(Side frontSide, Side rightSide, Side backSide, Side leftSide, Side downSide, Side upSide) {
        Cube cube = new Cube();
        cube.frontSide = frontSide;
        cube.rightSide = rightSide;
        cube.backSide = backSide;
        cube.leftSide = leftSide;
        cube.downSide = downSide;
        cube.upSide = upSide;
        return cube;
    }

    public void turn(SideTurnEnum sideTurnEnum, Direction direction) {
        TurnFactory.getTurn(sideTurnEnum).turn(this, direction);
    }

    public void output() {
        OutputUtils.output(this);
    }

    public Side getFrontSide() {
        return frontSide;
    }

    public Side getRightSide() {
        return rightSide;
    }

    public Side getBackSide() {
        return backSide;
    }

    public Side getLeftSide() {
        return leftSide;
    }

    public Side getDownSide() {
        return downSide;
    }

    public Side getUpSide() {
        return upSide;
    }
}
