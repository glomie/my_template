package com.temp.cube.result;

import com.temp.cube.turn.Move;

import java.util.List;

public class SolutionFormatter {

    public String format(Solution solution) {
        StringBuilder sb = new StringBuilder();
        sb.append("Scramble: ").append(solution.getScramble()).append("\n\n");
        sb.append("Cross: ").append(formatMoves(solution.getCrossMoves())).append("\n\n");
        sb.append("F2L:\n");
        formatF2L(solution.getF2lMoves(), sb);
        sb.append("\n");
        sb.append("OLL: ").append(formatMoves(solution.getOllMoves())).append("\n\n");
        sb.append("PLL: ").append(formatMoves(solution.getPllMoves())).append("\n\n");
        sb.append("Total moves: ").append(solution.getTotalMoves());
        return sb.toString();
    }

    public String formatCFOP(Solution solution) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== CFOP Solution ===\n\n");
        sb.append("Scramble:\n  ").append(solution.getScramble()).append("\n\n");

        sb.append("Cross (").append(Solution.countMoves(solution.getCrossMoves())).append(" moves):\n  ");
        sb.append(formatMoves(solution.getCrossMoves())).append("\n\n");

        sb.append("F2L (").append(Solution.countMoves(solution.getF2lMoves())).append(" moves):\n");
        formatF2L(solution.getF2lMoves(), sb);
        sb.append("\n");

        sb.append("OLL (").append(Solution.countMoves(solution.getOllMoves())).append(" moves):\n  ");
        sb.append(formatMoves(solution.getOllMoves())).append("\n\n");

        sb.append("PLL (").append(Solution.countMoves(solution.getPllMoves())).append(" moves):\n  ");
        sb.append(formatMoves(solution.getPllMoves())).append("\n\n");

        sb.append("==================\n");
        sb.append("Total: ").append(solution.getTotalMoves()).append(" moves");

        return sb.toString();
    }

    /** 把步骤序列拼成标准记法字符串（含转体 y 等）。引擎已合并相邻同向转动。 */
    public String formatMoves(List<Move> moves) {
        if (moves == null || moves.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(moves.get(i).notation());
        }
        return sb.toString();
    }

    /**
     * F2L 按转体(y)切分成若干小段，每段大致对应一个槽位的「转体 + 插入」，更直观。
     */
    private void formatF2L(List<Move> moves, StringBuilder sb) {
        if (moves == null || moves.isEmpty()) {
            sb.append("  (none)\n");
            return;
        }
        int slot = 1;
        StringBuilder line = new StringBuilder();
        for (Move m : moves) {
            if (m.isRotation() && line.length() > 0) {
                sb.append("  Slot ").append(slot++).append(": ").append(line).append("\n");
                line.setLength(0);
            }
            if (line.length() > 0) line.append(" ");
            line.append(m.notation());
        }
        if (line.length() > 0) sb.append("  Slot ").append(slot).append(": ").append(line).append("\n");
    }
}
