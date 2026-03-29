package com.temp.cube.solver;

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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < moves.size(); i++) {
            SideTurnAction action = moves.get(i);
            sb.append(action.getSideTurnEnum().name());
            if (action.getDirection() == Direction.COUNTERCLOCKWISE) {
                sb.append("'");
            }
            if (i < moves.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ScrambleGenerator generator = new ScrambleGenerator();
        String scramble = generator.generate(20);
        System.out.println("Generated scramble: " + scramble);
        
        ScrambleGenerator generator2 = new ScrambleGenerator(12345);
        String scramble2 = generator2.generate(20);
        System.out.println("Generated scramble with seed: " + scramble2);
    }
}
