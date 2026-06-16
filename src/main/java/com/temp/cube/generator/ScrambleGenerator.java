package com.temp.cube.generator;

import com.temp.cube.enums.Direction;
import com.temp.cube.enums.SideTurnEnum;
import com.temp.cube.turn.SideTurnAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrambleGenerator {

    private static final SideTurnEnum[] SIDES = {
        SideTurnEnum.R, SideTurnEnum.L, SideTurnEnum.U, SideTurnEnum.D, SideTurnEnum.F, SideTurnEnum.B
    };

    private static final SideTurnEnum[] OPPOSITES = {
        SideTurnEnum.L, SideTurnEnum.R, SideTurnEnum.D, SideTurnEnum.U, SideTurnEnum.B, SideTurnEnum.F
    };

    private static final int DEFAULT_LENGTH = 20;

    private Random random;

    public ScrambleGenerator() {
        this.random = new Random();
    }

    public ScrambleGenerator(long seed) {
        this.random = new Random(seed);
    }

    public String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public String generate(int length) {
        List<SideTurnAction> moves = generateMoves(length);
        return formatMoves(moves);
    }

    public String generate(long seed) {
        this.random = new Random(seed);
        return generate(DEFAULT_LENGTH);
    }

    public List<SideTurnAction> generateMoves(int length) {
        List<SideTurnAction> moves = new ArrayList<>();
        SideTurnEnum lastSide = null;
        SideTurnEnum secondLastSide = null;

        for (int i = 0; i < length; i++) {
            SideTurnEnum side;
            do {
                side = SIDES[random.nextInt(SIDES.length)];
            } while (side == lastSide || side == secondLastSide);

            Direction direction = random.nextBoolean() ? Direction.CLOCKWISE : Direction.COUNTERCLOCKWISE;
            
            if (random.nextInt(10) < 2) {
                direction = Direction.CLOCKWISE;
                moves.add(new SideTurnAction(side, Direction.CLOCKWISE));
                moves.add(new SideTurnAction(side, Direction.CLOCKWISE));
                i++;
            } else {
                moves.add(new SideTurnAction(side, direction));
            }

            secondLastSide = lastSide;
            lastSide = side;
        }

        return moves;
    }

    public List<SideTurnAction> parseMoves(String scramble) {
        List<SideTurnAction> moves = new ArrayList<>();
        String[] parts = scramble.trim().split("\\s+");

        for (String part : parts) {
            if (part.isEmpty()) continue;

            SideTurnEnum side = null;
            for (SideTurnEnum s : SIDES) {
                if (s.name().equals(part.substring(0, 1))) {
                    side = s;
                    break;
                }
            }

            if (side == null) continue;

            Direction direction = Direction.CLOCKWISE;
            if (part.contains("'")) {
                direction = Direction.COUNTERCLOCKWISE;
            }

            int count = 1;
            if (part.contains("2")) {
                count = 2;
            }

            for (int i = 0; i < count; i++) {
                moves.add(new SideTurnAction(side, direction));
            }
        }

        return moves;
    }

    private String formatMoves(List<SideTurnAction> moves) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < moves.size()) {
            SideTurnEnum side = moves.get(i).getSideTurnEnum();
            int net = 0; // 顺时针 +1，逆时针 -1，合并同面连续转动
            while (i < moves.size() && moves.get(i).getSideTurnEnum() == side) {
                net += moves.get(i).getDirection() == Direction.COUNTERCLOCKWISE ? -1 : 1;
                i++;
            }
            net = ((net % 4) + 4) % 4;
            switch (net) {
                case 1: tokens.add(side.name()); break;
                case 2: tokens.add(side.name() + "2"); break;
                case 3: tokens.add(side.name() + "'"); break;
                default: break; // 0：整体抵消，不输出
            }
        }
        return String.join(" ", tokens);
    }
}
