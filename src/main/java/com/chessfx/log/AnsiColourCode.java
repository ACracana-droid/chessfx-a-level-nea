package com.chessfx.log;

public enum AnsiColourCode {
    RESET("\u001B[0m"),
    YELLOW("\u001B[33m"), // warn
    GREEN("\u001B[32m"), // debug
    CYAN("\u001B[36m"), // info
    RED("\u001B[31m"); // error

    private final String code;

    AnsiColourCode(String s) {
        this.code = s;
    }

    @Override
    public String toString() {
        return code;
    }
}