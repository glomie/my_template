package com.temp.cube.solver;

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
        this.totalMoves = crossMoves.size() + f2lMoves.size() + ollMoves.size() + pllMoves.size();
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
