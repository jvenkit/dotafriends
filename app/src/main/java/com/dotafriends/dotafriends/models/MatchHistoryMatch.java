package com.dotafriends.dotafriends.models;

import java.util.List;

public class MatchHistoryMatch {
    private long matchId;
    private List<Player> players;

    public long getMatchId() {
        return matchId;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
