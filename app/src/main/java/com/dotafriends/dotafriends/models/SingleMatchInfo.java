package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for holding the information of a single match deserialised from JSON objects
 * fetched using the Dota 2 Web API. Should only be used to temporarily hold match information
 * retrieved from API calls before writing to the database.
 */
public class SingleMatchInfo extends WebApiResult{

    private List<PlayerMatchData> players = new ArrayList<>();
    private boolean radiantWin;
    private int duration;
    private int startTime;
    private long matchId;
    private int matchSeqNum;
    private int towerStatusRadiant;
    private int towerStatusDire;
    private int barracksStatusRadiant;
    private int barracksStatusDire;
    private int cluster;
    private int firstBloodTime;
    private int lobbyType;
    private int humanPlayers;
    private int leagueId;
    private int positiveVotes;
    private int negativeVotes;
    private int gameMode;
    private int engine;

    public List<PlayerMatchData> getPlayers() {
        return players;
    }

    public boolean isRadiantWin() {
        return radiantWin;
    }

    public int getDuration() {
        return duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public long getMatchId() {
        return matchId;
    }

    public int getMatchSeqNum() {
        return matchSeqNum;
    }

    public int getTowerStatusRadiant() {
        return towerStatusRadiant;
    }

    public int getTowerStatusDire() {
        return towerStatusDire;
    }

    public int getBarracksStatusRadiant() {
        return barracksStatusRadiant;
    }

    public int getBarracksStatusDire() {
        return barracksStatusDire;
    }

    public int getCluster() {
        return cluster;
    }

    public int getFirstBloodTime() {
        return firstBloodTime;
    }

    public int getLobbyType() {
        return lobbyType;
    }

    public int getHumanPlayers() {
        return humanPlayers;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public int getPositiveVotes() {
        return positiveVotes;
    }

    public int getNegativeVotes() {
        return negativeVotes;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getEngine() {
        return engine;
    }
}