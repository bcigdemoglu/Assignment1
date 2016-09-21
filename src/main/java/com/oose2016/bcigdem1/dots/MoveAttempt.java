package com.oose2016.bcigdem1.dots;

/**
 * Move model to handle move requests.
 */
@SuppressWarnings("unused")
public class MoveAttempt {

    private String playerId;
    private String row;
    private String col;

    public MoveAttempt() {
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getRow() {
        return row;
    }

    public String getCol() {
        return col;
    }
}
