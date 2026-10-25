package com.chessfx.chessconfig;

import com.chessfx.engine.board.Team;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

public class TeamConf {

    private final List<Team> teams = new ArrayList<>();
    Team white;
    int whiteIdx;
    Team black;
    int blackIdx;

    public TeamConf(String data) {
        Log.info("Constructing TeamConf class");
        int i = 0;
        int idx = 0;
        while (i < data.length()) {
            //  Team Colour | Direction Facing
            Team t = new Team(getTeamStyleClass(data.charAt(i)),
                    getVector(data.charAt(i + 1)), idx);
            assignFieldIfNecessary(t, data.charAt(i), i);
            teams.add(t);
            i += 3;
            idx++;
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
