package com.temp.cube.turn;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;

import java.util.List;

/**
 * 一步解法，三类之一：
 * <ul>
 *   <li>面转动（U/R/F/D/L/B）；</li>
 *   <li>整体转体（x/y/z）；</li>
 *   <li>复合转动（宽层 Rw.. / 中层 M E S）——以标准记法呈现，施加时展开成若干「面 + 转体」原子步。</li>
 * </ul>
 * quarter 表示 90°的圈数：1=顺时针, 2=180°, 3=逆时针。
 */
public class Move {

    private final SideTurnEnum face;        // 面转动时非空
    private final CubeTurnEnum rotation;    // 转体时非空
    private final int quarter;              // 1,2,3
    private final String compoundNotation;  // 复合转动（宽层/中层）时非空
    private final List<Move> expansion;      // 复合转动的原子展开（面/转体）

    private Move(SideTurnEnum face, CubeTurnEnum rotation, int quarter,
                 String compoundNotation, List<Move> expansion) {
        this.face = face;
        this.rotation = rotation;
        this.quarter = quarter;
        this.compoundNotation = compoundNotation;
        this.expansion = expansion;
    }

    public static Move face(SideTurnEnum face, int quarter) {
        return new Move(face, null, quarter, null, null);
    }

    public static Move rotation(CubeTurnEnum rotation, int quarter) {
        return new Move(null, rotation, quarter, null, null);
    }

    /** 复合转动（宽层/中层）：notation 为标准记法，expansion 为等价的「面+转体」原子序列。 */
    public static Move compound(String notation, int quarter, List<Move> expansion) {
        return new Move(null, null, quarter, notation, expansion);
    }

    /** 仅指整方块转体（x/y/z）；复合转动（宽层/中层）算作实际转动，不属此类。 */
    public boolean isRotation() {
        return rotation != null;
    }

    public boolean isCompound() {
        return compoundNotation != null;
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
        if (expansion != null) {
            for (Move m : expansion) m.apply(cube);
            return;
        }
        Direction dir = quarter == 3 ? Direction.COUNTERCLOCKWISE : Direction.CLOCKWISE;
        int times = quarter == 2 ? 2 : 1;
        for (int i = 0; i < times; i++) {
            if (face != null) cube.turn(face, dir);
            else cube.turn(rotation, dir);
        }
    }

    /** 标准记法：U / U2 / U' / y / Rw / Rw' / M2 等。 */
    public String notation() {
        if (compoundNotation != null) return compoundNotation;
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
