package com.temp.rubik;

public enum Move {
    R, R_PRIME, R2,
    L, L_PRIME, L2,
    U, U_PRIME, U2,
    D, D_PRIME, D2,
    F, F_PRIME, F2,
    B, B_PRIME, B2;

    public static Move parse(String s) {
        return switch (s.trim()) {
            case "R" -> R;
            case "R'" -> R_PRIME;
            case "R2" -> R2;
            case "L" -> L;
            case "L'" -> L_PRIME;
            case "L2" -> L2;
            case "U" -> U;
            case "U'" -> U_PRIME;
            case "U2" -> U2;
            case "D" -> D;
            case "D'" -> D_PRIME;
            case "D2" -> D2;
            case "F" -> F;
            case "F'" -> F_PRIME;
            case "F2" -> F2;
            case "B" -> B;
            case "B'" -> B_PRIME;
            case "B2" -> B2;
            default -> throw new IllegalArgumentException("Unknown move: " + s);
        };
    }

    public Move inverse() {
        return switch (this) {
            case R -> R_PRIME;
            case R_PRIME -> R;
            case L -> L_PRIME;
            case L_PRIME -> L;
            case U -> U_PRIME;
            case U_PRIME -> U;
            case D -> D_PRIME;
            case D_PRIME -> D;
            case F -> F_PRIME;
            case F_PRIME -> F;
            case B -> B_PRIME;
            case B_PRIME -> B;
            case R2, L2, U2, D2, F2, B2 -> this;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case R -> "R";
            case R_PRIME -> "R'";
            case R2 -> "R2";
            case L -> "L";
            case L_PRIME -> "L'";
            case L2 -> "L2";
            case U -> "U";
            case U_PRIME -> "U'";
            case U2 -> "U2";
            case D -> "D";
            case D_PRIME -> "D'";
            case D2 -> "D2";
            case F -> "F";
            case F_PRIME -> "F'";
            case F2 -> "F2";
            case B -> "B";
            case B_PRIME -> "B'";
            case B2 -> "B2";
        };
    }
}
