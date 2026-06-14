package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.SideTurnAction;

import java.util.List;

public class SolutionFormatter {

    public String format(Solution solution) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Scramble: ").append(solution.getScramble()).append("\n\n");
        
        sb.append("Cross: ");
        sb.append(formatMoves(solution.getCrossMoves()));
        sb.append("\n\n");
        
        sb.append("F2L:\n");
        formatF2L(solution.getF2lMoves(), sb);
        sb.append("\n");
        
        sb.append("OLL: ");
        sb.append(formatMoves(solution.getOllMoves()));
        sb.append("\n\n");
        
        sb.append("PLL: ");
        sb.append(formatMoves(solution.getPllMoves()));
        sb.append("\n\n");
        
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

    public String formatMoves(List<SideTurnAction> moves) {
        if (moves == null || moves.isEmpty()) {
            return "";
        }
        return compact(moves, 0, moves.size());
    }

    /**
     * 把连续的同面转动合并成标准记法：U U → U2，U U U → U'，U' → U'。
     * 内部步骤只有 90°（CLOCKWISE/COUNTERCLOCKWISE），这里仅用于展示。
     */
    private String compact(List<SideTurnAction> moves, int from, int to) {
        StringBuilder sb = new StringBuilder();
        int i = from;
        while (i < to) {
            SideTurnEnum face = moves.get(i).getSideTurnEnum();
            int quarter = 0;
            int j = i;
            while (j < to && moves.get(j).getSideTurnEnum() == face) {
                quarter += moves.get(j).getDirection() == Direction.CLOCKWISE ? 1 : 3;
                j++;
            }
            quarter %= 4;
            if (quarter != 0) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(face.name());
                if (quarter == 2) sb.append("2");
                else if (quarter == 3) sb.append("'");
            }
            i = j;
        }
        return sb.toString();
    }

    private void formatF2L(List<SideTurnAction> moves, StringBuilder sb) {
        if (moves == null || moves.isEmpty()) {
            sb.append("  (none)\n");
            return;
        }

        int slotSize = moves.size() / 4;
        for (int slot = 0; slot < 4; slot++) {
            int start = slot * slotSize;
            int end = (slot == 3) ? moves.size() : (slot + 1) * slotSize;
            sb.append("  Slot ").append(slot + 1).append(": ")
              .append(compact(moves, start, end)).append("\n");
        }
    }

    public String formatSimple(Solution solution) {
        StringBuilder sb = new StringBuilder();
        
        if (!solution.getCrossMoves().isEmpty()) {
            sb.append(formatMoves(solution.getCrossMoves())).append("\n");
        }
        
        if (!solution.getF2lMoves().isEmpty()) {
            sb.append(formatMoves(solution.getF2lMoves())).append("\n");
        }
        
        if (!solution.getOllMoves().isEmpty()) {
            sb.append(formatMoves(solution.getOllMoves())).append("\n");
        }
        
        if (!solution.getPllMoves().isEmpty()) {
            sb.append(formatMoves(solution.getPllMoves())).append("\n");
        }
        
        return sb.toString();
    }
}
