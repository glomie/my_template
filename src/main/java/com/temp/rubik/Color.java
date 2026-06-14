package com.temp.rubik;

public enum Color {
    WHITE('W'),
    YELLOW('Y'),
    GREEN('G'),
    BLUE('B'),
    RED('R'),
    ORANGE('O');

    private final char symbol;

    Color(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
