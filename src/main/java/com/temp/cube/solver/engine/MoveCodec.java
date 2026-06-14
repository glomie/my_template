package com.temp.cube.solver.engine;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部 token 与 {@link Move} 的转换。
 * <ul>
 *   <li>面转动 token = face*3 + (quarter-1)，face 顺序 U,R,F,D,L,B（0..17）。</li>
 *   <li>y 转体 token = {@link #ROT_Y_BASE} + (quarter-1)（18..20）。</li>
 * </ul>
 */
public final class MoveCodec {

    /** y 转体 token 起始值。 */
    public static final int ROT_Y_BASE = 18;

    private static final SideTurnEnum[] FACES = {
        SideTurnEnum.U, SideTurnEnum.R, SideTurnEnum.F,
        SideTurnEnum.D, SideTurnEnum.L, SideTurnEnum.B
    };

    private MoveCodec() {}

    public static List<Move> toMoves(int[] tokens) {
        List<Move> moves = new ArrayList<>();
        for (int t : tokens) {
            if (t >= ROT_Y_BASE) {
                moves.add(Move.rotation(CubeTurnEnum.y, t - ROT_Y_BASE + 1));
            } else {
                moves.add(Move.face(FACES[t / 3], t % 3 + 1));
            }
        }
        return moves;
    }
}
