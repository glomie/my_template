package com.temp.cube.model;

import com.temp.cube.enums.Color;
import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.output.OutputManager;
import com.temp.cube.turn.TurnFactory;

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

    /**
     * 以完成状态作为初始化, 默认按照国际标准: 白顶绿前
     */
    public static Cube init() {
        Side frontSide = Side.initWithOneColor(Color.GREEN);
        Side rightSide = Side.initWithOneColor(Color.RED);
        Side backSide = Side.initWithOneColor(Color.BLUE);
        Side leftSide = Side.initWithOneColor(Color.ORANGE);
        Side downSide = Side.initWithOneColor(Color.YELLOW);
        Side upSide = Side.initWithOneColor(Color.WHITE);
        return Cube.init(frontSide, rightSide, backSide, leftSide, downSide, upSide);
    }

    public void turn(SideTurnEnum sideTurnEnum, Direction direction) {
        TurnFactory.getTurn(sideTurnEnum).turn(this, direction);
    }

    public void turn(CubeTurnEnum cubeTurnEnum, Direction direction) {
        TurnFactory.getTurn(cubeTurnEnum).turn(this, direction);
    }

    public void output() {
        OutputManager.useDefaultOutput().output(this);
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

    public void setFrontSide(Side frontSide) {
        this.frontSide = frontSide;
    }

    public void setRightSide(Side rightSide) {
        this.rightSide = rightSide;
    }

    public void setBackSide(Side backSide) {
        this.backSide = backSide;
    }

    public void setLeftSide(Side leftSide) {
        this.leftSide = leftSide;
    }

    public void setDownSide(Side downSide) {
        this.downSide = downSide;
    }

    public void setUpSide(Side upSide) {
        this.upSide = upSide;
    }
}
