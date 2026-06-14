package com.temp.rubik;
public class TestCube {
    public static void main(String[] args) {
        // Test 1: R U R' U' applied 6 times should return to solved
        Cube c = new Cube();
        for (int i = 0; i < 6; i++) c.applySequence("R U R' U'");
        System.out.println("sexy*6 solved: " + c.isSolved());

        // Test 2: R R R R = identity
        c = new Cube();
        c.applySequence("R R R R");
        System.out.println("R4 solved: " + c.isSolved());

        // Test 3: R R' = identity
        c = new Cube();
        c.applySequence("R R'");
        System.out.println("R R' solved: " + c.isSolved());

        // Test 4: U U U U = identity
        c = new Cube();
        c.applySequence("U U U U");
        System.out.println("U4 solved: " + c.isSolved());

        // Test 5: superflip check - T perm twice = identity
        c = new Cube();
        c.applySequence("R U R' U' R' F R2 U' R' U' R U R' F'");
        c.applySequence("R U R' U' R' F R2 U' R' U' R U R' F'");
        System.out.println("T-perm*2 solved: " + c.isSolved());

        // Test 6: scramble and check cross
        c = new Cube();
        c.applySequence("R U R' U'");
        System.out.println("After R U R' U':");
        System.out.println("  D[1]=" + c.get(Cube.D,1) + " F[7]=" + c.get(Cube.F,7));
        System.out.println("  D[3]=" + c.get(Cube.D,3) + " L[7]=" + c.get(Cube.L,7));
        System.out.println("  D[5]=" + c.get(Cube.D,5) + " R[7]=" + c.get(Cube.R,7));
        System.out.println("  D[7]=" + c.get(Cube.D,7) + " B[7]=" + c.get(Cube.B,7));
        c.print();
    }
}
