package com.temp.rubik;
public class TestAlgs {
    public static void main(String[] args) {
        // Test Jb-perm (should be order 2 or specific cycle)
        Cube c = new Cube();
        c.applySequence("R U R' F' R U R' U' R' F R2 U' R'");
        c.applySequence("R U R' F' R U R' U' R' F R2 U' R'");
        System.out.println("Jb-perm*2 solved: " + c.isSolved());

        // Test Y-perm
        c = new Cube();
        c.applySequence("F R U' R' U' R U R' F' R U R' U' R' F R F'");
        c.applySequence("F R U' R' U' R U R' F' R U R' U' R' F R F'");
        System.out.println("Y-perm*2 solved: " + c.isSolved());

        // Test Ua-perm (order 3)
        c = new Cube();
        c.applySequence("R U' R U R U R U' R' U' R2");
        c.applySequence("R U' R U R U R U' R' U' R2");
        c.applySequence("R U' R U R U R U' R' U' R2");
        System.out.println("Ua-perm*3 solved: " + c.isSolved());

        // Test Ub-perm (order 3)
        c = new Cube();
        c.applySequence("R2 U R U R' U' R' U' R' U R'");
        c.applySequence("R2 U R U R' U' R' U' R' U R'");
        c.applySequence("R2 U R U R' U' R' U' R' U R'");
        System.out.println("Ub-perm*3 solved: " + c.isSolved());

        // Test H-perm (order 2)
        c = new Cube();
        c.applySequence("R2 U2 R U2 R2 U2 R2 U2 R U2 R2");
        c.applySequence("R2 U2 R U2 R2 U2 R2 U2 R U2 R2");
        System.out.println("H-perm*2 solved: " + c.isSolved());

        // Test Sune (order 6)
        c = new Cube();
        for (int i = 0; i < 6; i++) c.applySequence("R U2 R' U' R U' R'");
        System.out.println("Sune*6 solved: " + c.isSolved());

        // Test Anti-Sune (order 6)
        c = new Cube();
        for (int i = 0; i < 6; i++) c.applySequence("R U R' U R U2 R'");
        System.out.println("Anti-Sune*6 solved: " + c.isSolved());

        // Test OLL edge alg F R U R' U' F' (order?)
        c = new Cube();
        c.applySequence("F R U R' U' F'");
        c.applySequence("F R U R' U' F'");
        c.applySequence("F R U R' U' F'");
        c.applySequence("F R U R' U' F'");
        c.applySequence("F R U R' U' F'");
        c.applySequence("F R U R' U' F'");
        System.out.println("OLL-edge*6 solved: " + c.isSolved());

        // Check what Jb-perm does to corners
        c = new Cube();
        c.applySequence("R U R' F' R U R' U' R' F R2 U' R'");
        System.out.println("\nAfter Jb-perm:");
        System.out.println("F[0]=" + c.get(Cube.F,0) + " F[2]=" + c.get(Cube.F,2));
        System.out.println("R[0]=" + c.get(Cube.R,0) + " R[2]=" + c.get(Cube.R,2));
        System.out.println("B[0]=" + c.get(Cube.B,0) + " B[2]=" + c.get(Cube.B,2));
        System.out.println("L[0]=" + c.get(Cube.L,0) + " L[2]=" + c.get(Cube.L,2));
        System.out.println("F[1]=" + c.get(Cube.F,1) + " R[1]=" + c.get(Cube.R,1));
        System.out.println("B[1]=" + c.get(Cube.B,1) + " L[1]=" + c.get(Cube.L,1));
        c.print();
    }
}
