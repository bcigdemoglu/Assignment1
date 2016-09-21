package com.oose2016.bcigdem1.dots;

/**
 * Created by mbugrahanc on 9/20/16.
 */
public class Player {
    private String playerId;
    private String gameId;
    private String playerType;

    public Player(String playerId, String gameId, String playerType) {
        this.playerId = playerId;
        this.gameId = gameId;
        this.playerType = playerType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerType() {
        return playerType;
    }

    public String getGameId() {
        return gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player plyr = (Player) o;

        return playerId.equals(plyr.getPlayerId());
    }
}
