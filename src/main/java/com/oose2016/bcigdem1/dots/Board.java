package com.oose2016.bcigdem1.dots;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by mbugrahanc on 9/21/16.
 */
public class Board {

    private static transient final int DIM = 5;

    private HashSet<Line> horizontalLines;
    private HashSet<Line> verticalLines;
    private HashSet<Box> boxes;

    private transient boolean[][] dots = new boolean[DIM][DIM];
    private transient Player[] players = new Player[2];
    private transient int playersInGame = 0;
    private transient State state;

    /**
     * gameId: <String>, the ID of the joined game.
     */
    private transient String gameId;

    public Board(String gameId) {
        horizontalLines = new HashSet<>();
        verticalLines = new HashSet<>();
        boxes = new HashSet<>();

        for (int row = 0; row < DIM; row ++) {
            for (int col = 0; col < DIM; col++) {
                // Fill all dots
                this.dots[row][col] = false;

                // Add horizontal lines
                if (col != DIM - 1) {
                    horizontalLines.add(new Line(row, col, false));
                }

                // Add vertical lines
                if (row != DIM - 1) {
                    verticalLines.add(new Line(row, col, false));
                }

                // Add boxes
                if (row != DIM - 1 && col != DIM - 1) {
                    boxes.add(new Box(row, col, "NONE"));
                }

            }
        }
        this.gameId = gameId;
        this.state = new State();
    }

    public boolean addPlayer(Player player) {
        players[playersInGame++] = player;
        return true;
    }

    public String getRemainingColor() {
        return (players[0].getPlayerType().equals("BLUE")) ? "RED" : "BLUE";
    }

    public boolean checkPlayerInGame(String playerId) {
        for (Player p : players) {
            if (playerId.equals(p.getPlayerId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isBoardFull() {
        return playersInGame == 2;
    }

    public void startGame() {
        this.state.setState("IN_PROGRESS");
    }

    /**
     * @param row Row index
     * @param col Column index
     * @return True if the line is added, false if cannot be added
     */
    public boolean addVerticalLine(String row, String col) {
        return insertLine(verticalLines, row, col);
    }

    /**
     * @param row Row index
     * @param col Column index
     * @return True if the line is added, false if cannot be added
     */
    public boolean addHorizontalLine(String row, String col) {
        return insertLine(horizontalLines, row, col);
    }


    /**
     * @param strRow Row index
     * @param strCol Column index
     * @param playerId Id of the player making the move
     * @return int Score earned by the player
     */
    public int checkVerticalBox(String strRow, String strCol, String playerId) {
        int row = Integer.parseInt(strRow);
        int col = Integer.parseInt(strCol);

        dots[row][col] = true;
        dots[row + 1][col] = true;

        int score = 0;

        // Check complete right box if col + 1 < box dimensions
        if (col + 1 < DIM) {
            if (dots[row][col + 1] && dots[row + 1][col + 1]) {
                boxes.remove(new Box(row, col));
                boxes.add(new Box(row, col, getPlayerById(playerId).getPlayerType()));
                score++;
            }
        }

        // Check complete left box if col - 1 >= 0
        if (col - 1 >= 0) {
            if (dots[row][col - 1] && dots[row + 1][col - 1]) {
                boxes.remove(new Box(row, col - 1));
                boxes.add(new Box(row, col - 1, getPlayerById(playerId).getPlayerType()));
                score++;
            }
        }
        return score;

    }

    /**
     * @param strRow Row index
     * @param strCol Column index
     * @param playerId Id of the player making the move
     * @return int Score earned by the player
     */
    public int checkHorizontalBox(String strRow, String strCol, String playerId) {
        int row = Integer.parseInt(strRow);
        int col = Integer.parseInt(strCol);

        dots[row][col] = true;
        dots[row][col + 1] = true;

        int score = 0;

        // Check complete lower box if row + 1 < box dimensions
        if (row + 1 < DIM) {
            if (dots[row + 1][col] && dots[row + 1][col + 1]) {
                boxes.remove(new Box(row, col));
                boxes.add(new Box(row, col, getPlayerById(playerId).getPlayerType()));
                score++;
            }
        }

        // Check complete upper box if row - 1 >= 0
        if (row - 1 >= 0) {
            if (dots[row - 1][col] && dots[row - 1][col + 1]) {
                boxes.remove(new Box(row - 1, col));
                boxes.add(new Box(row - 1, col, getPlayerById(playerId).getPlayerType()));
                score++;
            }
        }
        return score;

    }

    private boolean insertLine(HashSet<Line> lineSet, String strRow, String strCol) {
        int row = Integer.parseInt(strRow);
        int col = Integer.parseInt(strCol);

        Line emptyLine = new Line(row, col, false);

        if (lineSet.contains(emptyLine)) {
            lineSet.remove(emptyLine);
            lineSet.add(new Line(row, col, true));
            return true;
        }

        return false;
    }

    public Player getPlayerById(String playerId) {
        return playerId.equals(players[0].getPlayerId()) ? players[0] : players[1];
    }

    public String getGameId() {
        return gameId;
    }

    public State getState() {
        return state;
    }
}
