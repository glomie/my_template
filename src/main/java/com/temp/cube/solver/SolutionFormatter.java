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
        
        sb.append("Cross (").append(solution.getCrossMoves().size()).append(" moves):\n  ");
        sb.append(formatMoves(solution.getCrossMoves())).append("\n\n");
        
        sb.append("F2L (").append(solution.getF2lMoves().size()).append(" moves):\n");
        formatF2L(solution.getF2lMoves(), sb);
        sb.append("\n");
        
        sb.append("OLL (").append(solution.getOllMoves().size()).append(" moves):\n  ");
        sb.append(formatMoves(solution.getOllMoves())).append("\n\n");
        
        sb.append("PLL (").append(solution.getPllMoves().size()).append(" moves):\n  ");
        sb.append(formatMoves(solution.getPllMoves())).append("\n\n");
        
        sb.append("==================\n");
        sb.append("Total: ").append(solution.getTotalMoves()).append(" moves");
        
        return sb.toString();
    }

    public String formatMoves(List<SideTurnAction> moves) {
        if (moves == null || moves.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            SideTurnAction action = moves.get(i);
            sb.append(formatAction(action));
            if (i < moves.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String formatAction(SideTurnAction action) {
        StringBuilder sb = new StringBuilder();
        sb.append(action.getSideTurnEnum().name());
        if (action.getDirection() == Direction.COUNTERCLOCKWISE) {
            sb.append("'");
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
            sb.append("  Slot ").append(slot + 1).append(": ");
            int start = slot * slotSize;
            int end = (slot == 3) ? moves.size() : (slot + 1) * slotSize;
            
            for (int i = start; i < end; i++) {
                sb.append(formatAction(moves.get(i)));
                if (i < end - 1) {
                    sb.append(" ");
                }
            }
            sb.append("\n");
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
