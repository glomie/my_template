package com.temp.cube.solver.formula;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PLLFormula {

    private static final Map<Integer, String[]> FORMULAS = new HashMap<>();

    static {
        FORMULAS.put(0, new String[]{});

        FORMULAS.put(1, new String[]{"R U R' U' R' F R2 U' R' U' R U R' F'"});

        FORMULAS.put(2, new String[]{"R' U R' U' R' F' R U R U' R2"});

        FORMULAS.put(3, new String[]{"R2 U R' U R U' R' U' R' F R2 U' R' U' R"});

        FORMULAS.put(4, new String[]{"R U' R U R U R U' R' U' R2"});

        FORMULAS.put(5, new String[]{"R2 U' R U' R U R' U R R"});

        FORMULAS.put(6, new String[]{"R U R' U R U R' F' R U R' U' R' F R"});

        FORMULAS.put(7, new String[]{"R' U R' U' R' F' R U R' U' R2"});

        FORMULAS.put(8, new String[]{"R2 U' R2 U R2 F R2 U' R2 U R2"});

        FORMULAS.put(9, new String[]{"R U R' F' R U R' U' R' F R2 U' R' U'"});

        FORMULAS.put(10, new String[]{"R' U R U R' F' R U R' U' R2 U R U R"});

        FORMULAS.put(11, new String[]{"R U' R' U' R U R D' R U' R' D R U R"});

        FORMULAS.put(12, new String[]{"R U R' U R' F R2 U' R' U R U R' F'"});

        FORMULAS.put(13, new String[]{"R2 U' R2 U' R2 U R2 U R2"});

        FORMULAS.put(14, new String[]{"R2 U R2 U' R2 U' R2 U R2"});

        FORMULAS.put(15, new String[]{"R U R' U' R' F R2 U' R' U' R U R' F'"});

        FORMULAS.put(16, new String[]{"R' U R' U' R' F' R U R' U' R2"});

        FORMULAS.put(17, new String[]{"R' U R' F R F' R' U' R' F' R F"});

        FORMULAS.put(18, new String[]{"R U R' U R U2 R' U' R' F R2 U' R' U' R U R' F'"});

        FORMULAS.put(19, new String[]{"R2 U R' U R' U' R U' R2 F R F'"});

        FORMULAS.put(20, new String[]{"R' U' R U' R U R' U R' F' R U R' U' R2"});

        FORMULAS.put(21, new String[]{"R U R' U' R' F R2 U' R' U' R U R' F'"});
    }

    public static String[] getFormula(int caseNumber) {
        return FORMULAS.getOrDefault(caseNumber, new String[]{});
    }

    public static int getFormulaCount() {
        return FORMULAS.size();
    }

    public static List<SideTurnAction> parseFormula(String formulaStr) {
        List<SideTurnAction> moves = new ArrayList<>();
        
        if (formulaStr == null || formulaStr.trim().isEmpty()) {
            return moves;
        }
        
        String[] parts = formulaStr.trim().split("\\s+");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            char base = part.charAt(0);
            boolean prime = part.contains("'");
            boolean doubleMove = part.contains("2");

            SideTurnEnum side = null;
            switch (base) {
                case 'R': side = SideTurnEnum.R; break;
                case 'L': side = SideTurnEnum.L; break;
                case 'U': side = SideTurnEnum.U; break;
                case 'D': side = SideTurnEnum.D; break;
                case 'F': side = SideTurnEnum.F; break;
                case 'B': side = SideTurnEnum.B; break;
            }

            if (side == null) continue;

            Direction direction = prime ? 
                Direction.COUNTERCLOCKWISE : 
                Direction.CLOCKWISE;

            int count = doubleMove ? 2 : 1;
            for (int i = 0; i < count; i++) {
                moves.add(new SideTurnAction(side, direction));
            }
        }

        return moves;
    }
}
