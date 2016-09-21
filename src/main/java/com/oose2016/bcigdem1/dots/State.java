package com.oose2016.bcigdem1.dots;

/**
 * Created by mbugrahanc on 9/21/16.
 */
public class State {
    /**
     * The number of boxes that the red player owns.
     */
    private int redScore;
    /**
     * The number of boxes that the blue player owns.
     */
    private int blueScore;
    /**
     * Either "WAITING_TO_START", "IN_PROGRESS", or "FINISHED"
     */
    private String state;
    /**
     * Either "RED", "BLUE", or "FINISHED"
     */
    private String whoseTurn;

    public State() {
        redScore = 0;
        blueScore = 0;
        state = "WAITING_TO_START";
        whoseTurn = "RED";
    }

    public void incScore(int score, String playerType) {
        if (playerType.equals("RED")) {
            incRedScore(score);
        } else {
            incBlueScore(score);
        }
    }

    public void incRedScore(int score) {
        this.redScore += score;
    }

    public void incBlueScore(int score) {
        this.blueScore += score;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void nextTurn() {
        if (!isFinished()) {
            whoseTurn = whoseTurn.equals("RED") ? "BLUE" : "RED";
        }
    }

    public String getState() {
        return state;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public boolean isFinished() {
        if (redScore + blueScore == 16) {
            state = "FINISHED";
            whoseTurn = "FINISHED";
            return true;
        }
        return false;
    }
}


