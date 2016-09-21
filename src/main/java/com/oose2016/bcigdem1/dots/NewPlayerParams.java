package com.oose2016.bcigdem1.dots;

import java.util.UUID;

/**
 * Create a random player id. Use to handle new player requests.
 */
public class NewPlayerParams {
    /**
     * playerId: <String>, player's ID (unique within the context of the game).
     */
    private final String playerId;

    /**
     * playerType: <String>, either "RED" or "BLUE"
     */
    @SuppressWarnings("unused")
    private String playerType;

    public NewPlayerParams() {
        this.playerId = UUID.randomUUID().toString();
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerType() {
        return playerType;
    }
}
