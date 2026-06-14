package com.temp.rubik;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== CFOP Rubik's Cube Solver ===");
        System.out.println("Enter scramble (e.g. R U R' U' F' ...): ");

        String scramble = scanner.nextLine().trim();
        if (scramble.isEmpty()) {
            System.out.println("No scramble provided, generating random...");
            scramble = generateScramble();
            System.out.println("Scramble: " + scramble);
        }

        Cube cube = new Cube();
        cube.applySequence(scramble);

        System.out.println("\nScrambled state:");
        cube.print();

        Solver solver = new Solver(cube);
        long start = System.currentTimeMillis();
        List<Move> solution = solver.solve();
        long elapsed = System.currentTimeMillis() - start;

        String solutionStr = solution.stream()
                .map(Move::toString)
                .collect(Collectors.joining(" "));

        System.out.println("\nSolution (" + solution.size() + " moves):");
        System.out.println(solutionStr);
        System.out.println("Time: " + elapsed + "ms");

        Cube verify = new Cube();
        verify.applySequence(scramble);
        verify.apply(solution);
        System.out.println("\nVerification: "
                + (verify.isSolved() ? "SOLVED" : "FAILED"));
    }

    private static String generateScramble() {
        String[] faces = {"R", "L", "U", "D", "F", "B"};
        String[] suffixes = {"", "'", "2"};
        StringBuilder sb = new StringBuilder();
        int last = -1;
        for (int i = 0; i < 20; i++) {
            int face;
            do { face = (int)(Math.random() * 6); } while (face == last);
            last = face;
            if (i > 0) sb.append(' ');
            sb.append(faces[face]).append(suffixes[(int)(Math.random()*3)]);
        }
        return sb.toString();
    }
}
