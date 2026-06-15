package com.temp.rubik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cube {

    public static final int U = 0, D = 1, F = 2, B = 3, L = 4, R = 5;

    private final int[][] state = new int[6][9];

    public Cube() {
        for (int face = 0; face < 6; face++) {
            Arrays.fill(state[face], face);
        }
    }

    public Cube(Cube other) {
        for (int i = 0; i < 6; i++) {
            System.arraycopy(other.state[i], 0, this.state[i], 0, 9);
        }
    }

    public int get(int face, int pos) {
        return state[face][pos];
    }

    public int center(int face) {
        return state[face][4];
    }

    public boolean isSolved() {
        for (int face = 0; face < 6; face++) {
            for (int i = 0; i < 9; i++) {
                if (state[face][i] != face) return false;
            }
        }
        return true;
    }

    public void apply(Move move) {
        switch (move) {
            case R -> rotateR();
            case R_PRIME -> { rotateR(); rotateR(); rotateR(); }
            case R2 -> { rotateR(); rotateR(); }
            case L -> rotateL();
            case L_PRIME -> { rotateL(); rotateL(); rotateL(); }
            case L2 -> { rotateL(); rotateL(); }
            case U -> rotateU();
            case U_PRIME -> { rotateU(); rotateU(); rotateU(); }
            case U2 -> { rotateU(); rotateU(); }
            case D -> rotateD();
            case D_PRIME -> { rotateD(); rotateD(); rotateD(); }
            case D2 -> { rotateD(); rotateD(); }
            case F -> rotateF();
            case F_PRIME -> { rotateF(); rotateF(); rotateF(); }
            case F2 -> { rotateF(); rotateF(); }
            case B -> rotateB();
            case B_PRIME -> { rotateB(); rotateB(); rotateB(); }
            case B2 -> { rotateB(); rotateB(); }
        }
    }

    public void apply(List<Move> moves) {
        for (Move m : moves) apply(m);
    }

    public void applySequence(String sequence) {
        if (sequence == null || sequence.isBlank()) return;
        for (String token : sequence.trim().split("\\s+")) {
            apply(Move.parse(token));
        }
    }

    private void rotateFaceCW(int face) {
        int[] f = state[face];
        int tmp = f[0];
        f[0] = f[6]; f[6] = f[8]; f[8] = f[2]; f[2] = tmp;
        tmp = f[1];
        f[1] = f[3]; f[3] = f[7]; f[7] = f[5]; f[5] = tmp;
    }

    private void rotateR() {
        rotateFaceCW(R);
        int[] tmp = {state[F][2], state[F][5], state[F][8]};
        state[F][2] = state[D][2]; state[F][5] = state[D][5]; state[F][8] = state[D][8];
        state[D][2] = state[B][6]; state[D][5] = state[B][3]; state[D][8] = state[B][0];
        state[B][6] = state[U][2]; state[B][3] = state[U][5]; state[B][0] = state[U][8];
        state[U][2] = tmp[0]; state[U][5] = tmp[1]; state[U][8] = tmp[2];
    }

    private void rotateL() {
        rotateFaceCW(L);
        int[] tmp = {state[F][0], state[F][3], state[F][6]};
        state[F][0] = state[U][0]; state[F][3] = state[U][3]; state[F][6] = state[U][6];
        state[U][0] = state[B][8]; state[U][3] = state[B][5]; state[U][6] = state[B][2];
        state[B][8] = state[D][0]; state[B][5] = state[D][3]; state[B][2] = state[D][6];
        state[D][0] = tmp[0]; state[D][3] = tmp[1]; state[D][6] = tmp[2];
    }

    private void rotateU() {
        rotateFaceCW(U);
        int[] tmp = {state[F][0], state[F][1], state[F][2]};
        state[F][0] = state[R][0]; state[F][1] = state[R][1]; state[F][2] = state[R][2];
        state[R][0] = state[B][0]; state[R][1] = state[B][1]; state[R][2] = state[B][2];
        state[B][0] = state[L][0]; state[B][1] = state[L][1]; state[B][2] = state[L][2];
        state[L][0] = tmp[0]; state[L][1] = tmp[1]; state[L][2] = tmp[2];
    }

    private void rotateD() {
        rotateFaceCW(D);
        int[] tmp = {state[F][6], state[F][7], state[F][8]};
        state[F][6] = state[L][6]; state[F][7] = state[L][7]; state[F][8] = state[L][8];
        state[L][6] = state[B][6]; state[L][7] = state[B][7]; state[L][8] = state[B][8];
        state[B][6] = state[R][6]; state[B][7] = state[R][7]; state[B][8] = state[R][8];
        state[R][6] = tmp[0]; state[R][7] = tmp[1]; state[R][8] = tmp[2];
    }

    private void rotateF() {
        rotateFaceCW(F);
        int[] tmp = {state[U][6], state[U][7], state[U][8]};
        state[U][6] = state[L][8]; state[U][7] = state[L][5]; state[U][8] = state[L][2];
        state[L][2] = state[D][0]; state[L][5] = state[D][1]; state[L][8] = state[D][2];
        state[D][0] = state[R][6]; state[D][1] = state[R][3]; state[D][2] = state[R][0];
        state[R][0] = tmp[0]; state[R][3] = tmp[1]; state[R][6] = tmp[2];
    }

    private void rotateB() {
        rotateFaceCW(B);
        int[] tmp = {state[U][0], state[U][1], state[U][2]};
        state[U][0] = state[R][2]; state[U][1] = state[R][5]; state[U][2] = state[R][8];
        state[R][2] = state[D][8]; state[R][5] = state[D][7]; state[R][8] = state[D][6];
        state[D][8] = state[L][6]; state[D][7] = state[L][3]; state[D][6] = state[L][0];
        state[L][0] = tmp[0]; state[L][3] = tmp[1]; state[L][6] = tmp[2];
    }

    public static List<Move> parseSequence(String sequence) {
        List<Move> moves = new ArrayList<>();
        if (sequence == null || sequence.isBlank()) return moves;
        for (String token : sequence.trim().split("\\s+")) {
            moves.add(Move.parse(token));
        }
        return moves;
    }

    private static final char[] FACE_CHARS = {'W', 'Y', 'G', 'B', 'O', 'R'};

    public void print() {
        printFaceRow(U, "      ");
        System.out.println("      -----");
        for (int row = 0; row < 3; row++) {
            StringBuilder sb = new StringBuilder();
            for (int face : new int[]{L, F, R, B}) {
                for (int col = 0; col < 3; col++) {
                    sb.append(FACE_CHARS[state[face][row * 3 + col]]);
                }
                sb.append('|');
            }
            System.out.println(sb);
        }
        System.out.println("      -----");
        printFaceRow(D, "      ");
    }

    private void printFaceRow(int face, String indent) {
        for (int row = 0; row < 3; row++) {
            StringBuilder sb = new StringBuilder(indent);
            for (int col = 0; col < 3; col++) {
                sb.append(FACE_CHARS[state[face][row * 3 + col]]);
            }
            System.out.println(sb);
        }
    }
}
