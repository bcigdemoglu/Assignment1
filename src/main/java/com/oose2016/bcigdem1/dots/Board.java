package com.oose2016.bcigdem1.dots;

import java.util.*;

/**
 * The game board model.
 */
public class Board {

    private static transient final int DIM = 5;

    private final HashSet<Line> horizontalLines;
    private final HashSet<Line> verticalLines;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final HashSet<Box> boxes;

    private final transient boolean[][] dots = new boolean[DIM][DIM];
    private final transient ArrayList<Player> players;
    private transient int playersInGame = 0;
    private final transient State state;

    public Board() {
        horizontalLines = new HashSet<>();
        verticalLines = new HashSet<>();
        boxes = new HashSet<>();
        players = new ArrayList<>();

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
        this.state = new State();
    }

    /**
     * @param player Player object
     */
    public void addPlayer(Player player) {
        players.add(player);
        playersInGame++;
    }

    /**
     * @return String color of the second player.
     */
    public String getRemainingColor() {
        return (players.get(0).getPlayerType().equals("BLUE")) ? "RED" : "BLUE";
    }

    public boolean checkPlayerInGame(String playerId) {
        for (Player p : players) {
            if (p.getPlayerId().equals(playerId)) { return true; }
        }
        return false;
    }

    /**
     * @return True if both players are already in game.
     */
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
            score += getBoxScore(row, col, playerId);
        }

        // Check complete left box if col - 1 >= 0
        if (col - 1 >= 0) {
            score += getBoxScore(row, col - 1, playerId);
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
            score += getBoxScore(row, col, playerId);
        }

        // Check complete upper box if row - 1 >= 0
        if (row - 1 >= 0) {
            score += getBoxScore(row - 1, col, playerId);
        }
        return score;

    }

    private int getBoxScore(int row, int col, String playerId) {
        if (checkBoxSides(row, col)) {
            boxes.remove(new Box(row, col));
            boxes.add(new Box(row, col, getPlayerById(playerId).getPlayerType()));
            return 1;
        }
        return 0;
    }

    private boolean checkBoxSides(int row, int col) {
        Box b = new Box(row, col);
        // Check horizontal lines
        ArrayList<Line> lineList = b.generateHorizontalLines();
        for (Line l : lineList) {
            if (!horizontalLines.contains(l)) { return false; }
        }
        // Check vertical lines
        lineList = b.generateVerticalLines();
        for (Line l : lineList) {
            if (!verticalLines.contains(l)) { return false; }
        }
        return true;
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
        return playerId.equals(players.get(0).getPlayerId()) ? players.get(0) : players.get(1);
    }

    public State getState() {
        state.updateState();
        return state;
    }
}
