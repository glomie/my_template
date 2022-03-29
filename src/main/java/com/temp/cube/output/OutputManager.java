package com.temp.cube.output;

public class OutputManager {

    private static final ConsolePrint consolePrint = new ConsolePrint();

    public static ConsolePrint useDefaultOutput() {
        return consolePrint;
    }
}
