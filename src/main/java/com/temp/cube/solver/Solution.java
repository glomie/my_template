package com.temp.cube.solver;

import com.temp.cube.turn.Move;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    private String scramble;

    private List<Move> crossMoves = new ArrayList<>();

    private List<Move> f2lMoves = new ArrayList<>();

    private List<Move> ollMoves = new ArrayList<>();

    private List<Move> pllMoves = new ArrayList<>();

    private int totalMoves;

    private boolean solved;

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public List<Move> getCrossMoves() {
        return crossMoves;
    }

    public void setCrossMoves(List<Move> crossMoves) {
        this.crossMoves = crossMoves;
    }

    public List<Move> getF2lMoves() {
        return f2lMoves;
    }

    public void setF2lMoves(List<Move> f2lMoves) {
        this.f2lMoves = f2lMoves;
    }

    public List<Move> getOllMoves() {
        return ollMoves;
    }

    public void setOllMoves(List<Move> ollMoves) {
        this.ollMoves = ollMoves;
    }

    public List<Move> getPllMoves() {
        return pllMoves;
    }

    public void setPllMoves(List<Move> pllMoves) {
        this.pllMoves = pllMoves;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public void calculateTotalMoves() {
        this.totalMoves = countMoves(crossMoves) + countMoves(f2lMoves)
                        + countMoves(ollMoves) + countMoves(pllMoves);
    }

    /** 计步（HTM 口径）：转体不计步，面转动每个标准记法 token 记 1 步（U2 也是 1 步）。 */
    public static int countMoves(List<Move> moves) {
        if (moves == null) return 0;
        int count = 0;
        for (Move m : moves) if (!m.isRotation()) count++;
        return count;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void addCrossMoves(List<Move> moves) {
        this.crossMoves.addAll(moves);
    }

    public void addF2lMoves(List<Move> moves) {
        this.f2lMoves.addAll(moves);
    }

    public void addOllMoves(List<Move> moves) {
        this.ollMoves.addAll(moves);
    }

    public void addPllMoves(List<Move> moves) {
        this.pllMoves.addAll(moves);
    }
}
