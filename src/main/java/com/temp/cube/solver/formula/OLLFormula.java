package com.temp.cube.solver.formula;

import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OLLFormula {

    private static final Map<Integer, String[]> FORMULAS = new HashMap<>();

    static {
        FORMULAS.put(1, new String[]{"R U2 R' U' R U' R'"});

        FORMULAS.put(2, new String[]{"R U R' U R U2 R'"});

        FORMULAS.put(3, new String[]{"R U2 R' U2 R U' R'"});

        FORMULAS.put(4, new String[]{"R U R' U' R' F R2 U R' U' F'"});

        FORMULAS.put(5, new String[]{"R' F R2 U' R' U R' F R"});

        FORMULAS.put(6, new String[]{"R U2 R' U' R U R' U' R U' R'"});

        FORMULAS.put(7, new String[]{"R U R' U R U2 R' U' R U' R'"});

        FORMULAS.put(8, new String[]{"R2 D' R U2 R' D R U2 R"});

        FORMULAS.put(9, new String[]{"R U R' U R U2 R' F R U R' U' F'"});

        FORMULAS.put(10, new String[]{"R' F R2 U R' F R F' U' R"});

        FORMULAS.put(11, new String[]{"R' F' R U R U' R' F R U R'"});

        FORMULAS.put(12, new String[]{"R' F R U' R' F' R F U F'"});

        FORMULAS.put(13, new String[]{"R2 U R' F R U R2 U' R' F' R"});

        FORMULAS.put(14, new String[]{"R U2 R' U' R U' R2 U2 R U R' U R"});

        FORMULAS.put(15, new String[]{"R U2 R2 U' R2 U' R2 U2 R"});

        FORMULAS.put(16, new String[]{"R' U2 R2 U R2 U R2 U2 R'"});

        FORMULAS.put(17, new String[]{"R U R' U' R' F R2 U R' U' R U R' F'"});

        FORMULAS.put(18, new String[]{"R U2 R' U2 R' F R2 U R' U' R' F' R"});

        FORMULAS.put(19, new String[]{"R' U2 R F R2 U' R U R' F' R"});

        FORMULAS.put(20, new String[]{"M U R U R' U' R' F R2 U' R' U' R U R' F'"});

        FORMULAS.put(21, new String[]{"R' F R2 U' R' F R F' U R"});

        FORMULAS.put(22, new String[]{"R U2 R' U' R U R' U' R U' R'"});

        FORMULAS.put(23, new String[]{"R U R' U R U2 R' U R U' R'"});

        FORMULAS.put(24, new String[]{"R U R' U R U2 R' U' R U' R' U' R U R'"});

        FORMULAS.put(25, new String[]{"R' U' R U' R' U2 R U R' U R"});

        FORMULAS.put(26, new String[]{"R' U R' U' R' F R2 U' R' U R"});

        FORMULAS.put(27, new String[]{"R' F' R U R U' R' F R2 U2 R' U' R"});

        FORMULAS.put(28, new String[]{"R U R' U R U' R' U' R' F R F'"});

        FORMULAS.put(29, new String[]{"R' U' R U' R' F R U R' F' R"});

        FORMULAS.put(30, new String[]{"R U R2 U' R' F R U R0"});

        FORMULAS.put(31, new String[]{"R' F R2 U' R' F R F' U2 R"});

        FORMULAS.put(32, new String[]{"R U R' U' R' F R2 U' R' U' R U R' F'"});

        FORMULAS.put(33, new String[]{"R U R' U' R' F R2 U' R' F' R U R"});

        FORMULAS.put(34, new String[]{"R' F R2 U' R' U R' F' R U R"});

        FORMULAS.put(35, new String[]{"R U2 R' U' R U' R' U' R U R'"});

        FORMULAS.put(36, new String[]{"R U2 R2 U' R U' R' U2 F R F'"});

        FORMULAS.put(37, new String[]{"R' U2 R2 U R' U R U2 F R F'"});

        FORMULAS.put(38, new String[]{"R U2 R' U' R U' R' U R' F R F'"});

        FORMULAS.put(39, new String[]{"R U R' U R U2 R' U2 R' F R F'"});

        FORMULAS.put(40, new String[]{"R' U' R U' R' U2 R U2 R' F R F'"});

        FORMULAS.put(41, new String[]{"R U2 R2 U' R2 U' R2 U2 R"});

        FORMULAS.put(42, new String[]{"R2 U2 R' U2 R2 U2 R"});

        FORMULAS.put(43, new String[]{"R' U2 R2 U R2 U R2 U2 R"});

        FORMULAS.put(44, new String[]{"R U R' U' R' F R F' R U R'"});

        FORMULAS.put(45, new String[]{"R U R' U' R' F R F' R' U' R"});

        FORMULAS.put(46, new String[]{"R' U' R U R' F' R F R' U R"});

        FORMULAS.put(47, new String[]{"R' U' R U R' F' R U' R' F R"});

        FORMULAS.put(48, new String[]{"R U2 R' U' R' F R F' U R U' R'"});

        FORMULAS.put(49, new String[]{"R U R' U' R' F R2 U' R' U R' F' R"});

        FORMULAS.put(50, new String[]{"R' F R2 U' R' U R' F' R U R U2 R'"});

        FORMULAS.put(51, new String[]{"R U R' U R U2 R' U' R' F R F'"});

        FORMULAS.put(52, new String[]{"R' U' R U' R' U2 R U R' F R F'"});

        FORMULAS.put(53, new String[]{"R U2 R' U' R U' R' F R U R' F'"});

        FORMULAS.put(54, new String[]{"R' U2 R U R' F' R U R' F R"});

        FORMULAS.put(55, new String[]{"R U R' U R U2 R' F R U R' U' F'"});

        FORMULAS.put(56, new String[]{"R' U' R U' R' U2 R F R U R' U' F'"});

        FORMULAS.put(57, new String[]{"R U2 R' U2 R' F R2 U R' U' F' R"});
    }

    public static String[] getFormula(int caseNumber) {
        return FORMULAS.getOrDefault(caseNumber, new String[]{"R U R' U' R' F R F'"});
    }

    public static int getFormulaCount() {
        return FORMULAS.size();
    }

    public static List<SideTurnAction> parseFormula(String formulaStr) {
        List<SideTurnAction> moves = new ArrayList<>();
        String[] parts = formulaStr.trim().split("\\s+");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            char base = part.charAt(0);
            boolean prime = part.contains("'");
            boolean doubleMove = part.contains("2");

            com.temp.cube.enums.SideTurnEnum side = null;
            switch (base) {
                case 'R': side = com.temp.cube.enums.SideTurnEnum.R; break;
                case 'L': side = com.temp.cube.enums.SideTurnEnum.L; break;
                case 'U': side = com.temp.cube.enums.SideTurnEnum.U; break;
                case 'D': side = com.temp.cube.enums.SideTurnEnum.D; break;
                case 'F': side = com.temp.cube.enums.SideTurnEnum.F; break;
                case 'B': side = com.temp.cube.enums.SideTurnEnum.B; break;
            }

            if (side == null) continue;

            com.temp.cube.enums.Direction direction = prime ? 
                com.temp.cube.enums.Direction.COUNTERCLOCKWISE : 
                com.temp.cube.enums.Direction.CLOCKWISE;

            int count = doubleMove ? 2 : 1;
            for (int i = 0; i < count; i++) {
                moves.add(new SideTurnAction(side, direction));
            }
        }

        return moves;
    }
}
