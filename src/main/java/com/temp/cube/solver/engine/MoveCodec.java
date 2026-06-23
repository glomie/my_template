package com.temp.cube.solver.engine;

import com.temp.cube.enums.CubeTurnEnum;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * 内部 token 与 {@link Move} 的转换。token = kind*3 + (amount-1)，amount 1/2/3 = 90°CW/180°/90°CCW。
 * kind 分配：
 * <ul>
 *   <li>0..5：面转动 U R F D L B；</li>
 *   <li>6/7/8：整方块转体 y x z（token 18..26）；</li>
 *   <li>9..14：宽层 Rw Lw Uw Dw Fw Bw（token 27..44）；</li>
 *   <li>15/16/17：中层 M E S（token 45..53）。</li>
 * </ul>
 * 宽层/中层在输出时以标准记法呈现，施加到 {@link com.temp.cube.model.Cube} 时展开成「面 + 转体」原子步。
 */
public final class MoveCodec {

    /** y 转体 token 起始值（kind 6）。F2L 仍按此基址产出 y 转体。 */
    public static final int ROT_Y_BASE = 18;

    private static final SideTurnEnum[] FACES = {
        SideTurnEnum.U, SideTurnEnum.R, SideTurnEnum.F,
        SideTurnEnum.D, SideTurnEnum.L, SideTurnEnum.B
    };

    /** kind 6/7/8 → 转体枚举。 */
    private static final CubeTurnEnum[] ROTS = { CubeTurnEnum.y, CubeTurnEnum.x, CubeTurnEnum.z };

    /** 宽层/中层 kind（9..17）的标准记法名。 */
    private static final String[] COMPOUND_NAME = {
        "Rw", "Lw", "Uw", "Dw", "Fw", "Bw", "M", "E", "S"
    };

    private MoveCodec() {}

    public static List<Move> toMoves(int[] tokens) {
        List<Move> moves = new ArrayList<>();
        for (int t : tokens) {
            int kind = t / 3, q = t % 3 + 1;
            if (kind < 6) {
                moves.add(Move.face(FACES[kind], q));
            } else if (kind < 9) {
                moves.add(Move.rotation(ROTS[kind - 6], q));
            } else {
                String base = COMPOUND_NAME[kind - 9];
                String notation = base + (q == 2 ? "2" : q == 3 ? "'" : "");
                moves.add(Move.compound(notation, q, expand(kind, q)));
            }
        }
        return moves;
    }

    /** 宽层/中层的「面+转体」原子展开（unit 重复 q 次），与 {@link FaceCube} 的排列定义一致。 */
    private static List<Move> expand(int kind, int q) {
        List<Move> unit = new ArrayList<>();
        switch (kind) {
            case 9:  add(unit, SideTurnEnum.L, 1); add(unit, CubeTurnEnum.x, 1); break;  // Rw = L x
            case 10: add(unit, SideTurnEnum.R, 1); add(unit, CubeTurnEnum.x, 3); break;  // Lw = R x'
            case 11: add(unit, SideTurnEnum.D, 1); add(unit, CubeTurnEnum.y, 1); break;  // Uw = D y
            case 12: add(unit, SideTurnEnum.U, 1); add(unit, CubeTurnEnum.y, 3); break;  // Dw = U y'
            case 13: add(unit, SideTurnEnum.B, 1); add(unit, CubeTurnEnum.z, 1); break;  // Fw = B z
            case 14: add(unit, SideTurnEnum.F, 1); add(unit, CubeTurnEnum.z, 3); break;  // Bw = F z'
            case 15: add(unit, SideTurnEnum.R, 1); add(unit, CubeTurnEnum.x, 3); add(unit, SideTurnEnum.L, 3); break; // M = R x' L'
            case 16: add(unit, SideTurnEnum.U, 1); add(unit, CubeTurnEnum.y, 3); add(unit, SideTurnEnum.D, 3); break; // E = U y' D'
            case 17: add(unit, SideTurnEnum.B, 1); add(unit, CubeTurnEnum.z, 1); add(unit, SideTurnEnum.F, 3); break; // S = B z F'
            default: throw new IllegalArgumentException("not compound kind: " + kind);
        }
        List<Move> out = new ArrayList<>();
        for (int r = 0; r < q; r++) out.addAll(unit);
        return out;
    }

    private static void add(List<Move> list, SideTurnEnum face, int quarter) {
        list.add(Move.face(face, quarter));
    }

    private static void add(List<Move> list, CubeTurnEnum rot, int quarter) {
        list.add(Move.rotation(rot, quarter));
    }
}
