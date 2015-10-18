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

    public List<PlayerMatchData> players = new ArrayList<>();
    public boolean radiantWin;
    public int duration;
    public int startTime;
    public long matchId;
    public int matchSeqNum;
    public int towerStatusRadiant;
    public int towerStatusDire;
    public int barracksStatusRadiant;
    public int barracksStatusDire;
    public int cluster;
    public int firstBloodTime;
    public int lobbyType;
    public int humanPlayers;
    public int leagueId;
    public int positiveVotes;
    public int negativeVotes;
    public int gameMode;
    public int engine;

    public class PlayerMatchData {

        public long accountId;
        public int playerSlot;
        public int heroId;
        @SerializedName("item_0")
        public int item0;
        @SerializedName("item_1")
        public int item1;
        @SerializedName("item_2")
        public int item2;
        @SerializedName("item_3")
        public int item3;
        @SerializedName("item_4")
        public int item4;
        @SerializedName("item_5")
        public int item5;
        public int kills;
        public int deaths;
        public int assists;
        public int leaverStatus;
        public int gold;
        public int lastHits;
        public int denies;
        public int goldPerMin;
        public int xpPerMin;
        public int goldSpent;
        public int heroDamage;
        public int towerDamage;
        public int heroHealing;
        public int level;
        public List<AbilityUpgrade> abilityUpgrades = new ArrayList<>();

        public class AbilityUpgrade {

            public int ability;
            public int time;
            public int level;

        }

    }

}