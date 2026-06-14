package com.temp.cube.turn;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;

/**
 * 一步解法：要么是一个面转动（U/R/F/D/L/B），要么是一个整体转体（x/y/z）。
 * quarter 表示 90°的圈数：1=顺时针, 2=180°, 3=逆时针。
 */
public class Move {

    private final SideTurnEnum face;       // 面转动时非空
    private final CubeTurnEnum rotation;   // 转体时非空
    private final int quarter;             // 1,2,3

    private Move(SideTurnEnum face, CubeTurnEnum rotation, int quarter) {
        this.face = face;
        this.rotation = rotation;
        this.quarter = quarter;
    }

    public static Move face(SideTurnEnum face, int quarter) {
        return new Move(face, null, quarter);
    }

    public static Move rotation(CubeTurnEnum rotation, int quarter) {
        return new Move(null, rotation, quarter);
    }

    public boolean isRotation() {
        return rotation != null;
    }

    public SideTurnEnum getFace() {
        return face;
    }

    public CubeTurnEnum getRotation() {
        return rotation;
    }

    public int getQuarter() {
        return quarter;
    }

    /** 施加到 model.Cube。 */
    public void apply(Cube cube) {
        Direction dir = quarter == 3 ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
        int times = quarter == 2 ? 2 : 1;
        for (int i = 0; i < times; i++) {
            if (face != null) cube.turn(face, dir);
            else cube.turn(rotation, dir);
        }
    }

    /** 标准记法：U / U2 / U' / y / y2 / y' 等。 */
    public String notation() {
        String base = face != null ? face.name() : rotation.name();
        if (quarter == 2) return base + "2";
        if (quarter == 3) return base + "'";
        return base;
    }

    @Override
    public String toString() {
        return notation();
    }
}
