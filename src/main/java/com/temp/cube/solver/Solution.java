package com.temp.cube.solver;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    private String scramble;

    private List<SideTurnAction> crossMoves = new ArrayList<>();

    private List<SideTurnAction> f2lMoves = new ArrayList<>();

    private List<SideTurnAction> ollMoves = new ArrayList<>();

    private List<SideTurnAction> pllMoves = new ArrayList<>();

    private int totalMoves;

    private boolean solved;

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public List<SideTurnAction> getCrossMoves() {
        return crossMoves;
    }

    public void setCrossMoves(List<SideTurnAction> crossMoves) {
        this.crossMoves = crossMoves;
    }

    public List<SideTurnAction> getF2lMoves() {
        return f2lMoves;
    }

    public void setF2lMoves(List<SideTurnAction> f2lMoves) {
        this.f2lMoves = f2lMoves;
    }

    public List<SideTurnAction> getOllMoves() {
        return ollMoves;
    }

    public void setOllMoves(List<SideTurnAction> ollMoves) {
        this.ollMoves = ollMoves;
    }

    public List<SideTurnAction> getPllMoves() {
        return pllMoves;
    }

    public void setPllMoves(List<SideTurnAction> pllMoves) {
        this.pllMoves = pllMoves;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public void calculateTotalMoves() {
        // 按标准记法计步：连续同面合并，U2 记 1 步
        this.totalMoves = countMoves(crossMoves) + countMoves(f2lMoves)
                        + countMoves(ollMoves) + countMoves(pllMoves);
    }

    /** 统计标准记法下的步数：相邻同面转动合并，净转动非 0 记 1 步。 */
    public static int countMoves(List<SideTurnAction> moves) {
        if (moves == null || moves.isEmpty()) return 0;
        int count = 0;
        int i = 0;
        while (i < moves.size()) {
            SideTurnEnum face = moves.get(i).getSideTurnEnum();
            int quarter = 0;
            while (i < moves.size() && moves.get(i).getSideTurnEnum() == face) {
                quarter += moves.get(i).getDirection() == Direction.CLOCKWISE ? 1 : 3;
                i++;
            }
            if (quarter % 4 != 0) count++;
        }
        return count;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void addCrossMoves(List<SideTurnAction> moves) {
        this.crossMoves.addAll(moves);
    }

    public void addF2lMoves(List<SideTurnAction> moves) {
        this.f2lMoves.addAll(moves);
    }

    public void addOllMoves(List<SideTurnAction> moves) {
        this.ollMoves.addAll(moves);
    }

    public void addPllMoves(List<SideTurnAction> moves) {
        this.pllMoves.addAll(moves);
    }
}
