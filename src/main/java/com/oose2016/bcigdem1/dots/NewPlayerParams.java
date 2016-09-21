package com.oose2016.bcigdem1.dots;

import java.util.UUID;

/**
 * Created by mbugrahanc on 9/20/16.
 */
public class NewPlayerParams {
    /**
     * playerId: <String>, player's ID (unique within the context of the game).
     */
    private String playerId;

    /**
     * playerType: <String>, either "RED" or "BLUE"
     */
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
