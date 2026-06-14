package com.temp.cube.solver.engine;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.model.Cube;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部 move 编号（face*3+amount）与 {@link SideTurnAction} / {@link Cube} 转动之间的转换。
 * face 顺序 U,R,F,D,L,B；amount 0=90°顺,1=180°,2=90°逆。
 */
public final class MoveCodec {

    private static final SideTurnEnum[] FACES = {
        SideTurnEnum.U, SideTurnEnum.R, SideTurnEnum.F,
        SideTurnEnum.D, SideTurnEnum.L, SideTurnEnum.B
    };

    private MoveCodec() {}

    /** 内部 move 序列转成 SideTurnAction 列表（180° 拆成两次顺时针）。 */
    public static List<SideTurnAction> toActions(int[] moves) {
        List<SideTurnAction> actions = new ArrayList<>();
        for (int m : moves) {
            SideTurnEnum face = FACES[m / 3];
            int amount = m % 3;
            switch (amount) {
                case 0:
                    actions.add(new SideTurnAction(face, Direction.CLOCKWISE));
                    break;
                case 1:
                    actions.add(new SideTurnAction(face, Direction.CLOCKWISE));
                    actions.add(new SideTurnAction(face, Direction.CLOCKWISE));
                    break;
                default:
                    actions.add(new SideTurnAction(face, Direction.COUNTERCLOCKWISE));
                    break;
            }
        }
        return actions;
    }

    /** 把动作施加到 model.Cube。 */
    public static void apply(Cube cube, List<SideTurnAction> actions) {
        for (SideTurnAction a : actions) cube.turn(a.getSideTurnEnum(), a.getDirection());
    }
}
