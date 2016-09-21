package com.oose2016.bcigdem1.dots;

/**
 * Player model to keep track of player and game ids, as well as player Type.
 */
public class Player {
    private final String playerId;
    private final String playerType;
    private final String gameId;

    public Player(String playerId, String playerType, String gameId) {
        this.playerId = playerId;
        this.playerType = playerType;
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerType() {
        return playerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player plyr = (Player) o;

        return playerId.equals(plyr.getPlayerId());
    }
}
