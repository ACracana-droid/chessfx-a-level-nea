package com.chessfx.chessconfig;

import com.chessfx.engine.pieces.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamConf {

    private final List<Team> teams = new ArrayList<>();
    Team white;
    int whiteIdx;
    Team black;
    int blackIdx;

    public TeamConf(String data) {
        System.out.println("Constructing TeamConf");
        int i = 0;
        while (i < data.length()) {
            //  Team Colour | Direction Facing | Player Type
            Team t = new Team(getTeamStyleClass(data.charAt(i)),
                    getVector(data.charAt(i + 1)), data.charAt(i + 2));
            assignFieldIfNecessary(t, data.charAt(i), i);
            teams.add(t);
            i += 4;
        }

    }

    private void assignFieldIfNecessary(Team t, char colour, int i) {
        if (colour == 'w') {
            white = t;
            whiteIdx = i;
        }
        if (colour == 'b') {
            black = t;
            blackIdx = i;
        }
    }

    private int[] UP = {1, 1};
    private int[] DOWN = {-1, 1};
    // Untested and unimportant:
    private int[] LEFT = {1, -1};
    private int[] RIGHT = {-1, -1};

    private int[] getVector(char c) {
        if (c == 'u')
            return UP;
        if (c == 'd')
            return DOWN;
        return new int[]{0, 0};
    }

    private String getTeamStyleClass(char c) {
        switch (c) {
            case 'w' -> {
                return "light";
            }
            case 'b' -> {
                return "dark";
            }
            case 'r' -> {
                return "red";
            }
            case 'B' -> {
                return "blue";
            }
            case 'y' -> {
                return "yellow";
            }
            default -> {
                return null;
            }
        }
    }

    public List<Team> getTeams() {
        return teams;
    }
}
