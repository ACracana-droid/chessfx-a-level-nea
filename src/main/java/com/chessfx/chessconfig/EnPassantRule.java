package com.chessfx.chessconfig;

public enum EnPassantRule {
    DEFAULT(),
    ETERNAL(),
    NEVER();

    public static EnPassantRule parseRule(String ruleString) {
        for (EnPassantRule enPassantRule : EnPassantRule.values()) {
            if (ruleString.equals(enPassantRule.name().toLowerCase())) return enPassantRule;
        }
        return DEFAULT;
    }
}
