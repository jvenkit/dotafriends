package com.dotafriends.dotafriends.database;

import android.provider.BaseColumns;

/**
 * Defines the column and table names of the database. Each inner class represents a
 * table.
 */
public class DatabaseContract {

    /**
     * Empty constructor prevents accidental instantiation
     */
    public DatabaseContract() {}

    public static abstract class MatchInfo implements BaseColumns {
        public static final String TABLE_NAME = "matchinfo";
        public static final String RADIANT_WIN = "radiantwin";
        public static final String DURATION = "duration";
        public static final String START_TIME = "starttime";
        public static final String MATCH_SEQ_NUM = "matchseqnum";
        public static final String TOWER_STATUS_RADIANT = "towerstatusradiant";
        public static final String TOWER_STATUS_DIRE = "towerstatusdire";
        public static final String BARRACKS_STATUS_RADIANT = "barrackstatusradiant";
        public static final String BARRACKS_STATUS_DIRE = "barracksstatusdire";
        public static final String CLUSTER = "cluster";
        public static final String FIRST_BLOOD_TIME = "firstbloodtime";
        public static final String LOBBY_TYPE = "lobbytype";
        public static final String HUMAN_PLAYERS = "humanplayers";
        public static final String LEAGUE_ID = "leagueId";
        public static final String POSITIVE_VOTES = "positivevotes";
        public static final String NEGATIVE_VOTES = "negativevote";
        public static final String GAME_MODE = "gamemode";
        public static final String ENGINE = "engine";
    }

    public static abstract class Players implements BaseColumns {
        public static final String TABLE_NAME = "players";
        public static final String ACCOUNT_ID = "accountid";
        public static final String DISPLAY_NAME = "displayname";
        public static final String WINS_WITH = "winswith";
        public static final String LOSSES_WITH = "losseswith";
        public static final String WINS_AGAINST = "winsagainst";
        public static final String LOSSES_AGAINST = "lossesagainst";
    }

    public static final class PlayerMatchData {
        public static final String TABLE_NAME = "playermatchdata";
        public static final String MATCH_ID = "matchid";
        public static final String ACCOUNT_ID = "accountid";
        public static final String PLAYER_SLOT = "playerslot";
        public static final String HERO_ID = "heroid";
        public static final String ITEM_0 = "item0";
        public static final String ITEM_1 = "item1";
        public static final String ITEM_2 = "item2";
        public static final String ITEM_3 = "item3";
        public static final String ITEM_4 = "item4";
        public static final String ITEM_5 = "item5";
        public static final String KILLS = "kills";
        public static final String DEATHS = "deaths";
        public static final String ASSISTS = "assists";
        public static final String LEAVER_STATUS = "leaverstatus";
        public static final String GOLD = "gold";
        public static final String LAST_HITS = "lasthits";
        public static final String DENIES = "denies";
        public static final String GOLD_PER_MIN = "goldpermin";
        public static final String XP_PER_MIN = "xppermin";
        public static final String GOLD_SPENT = "goldspent";
        public static final String HERO_DAMAGE = "herodamage";
        public static final String TOWER_DAMAGE = "towerdamage";
        public static final String HERO_HEALING = "herohealing";
        public static final String LEVEL = "level";
    }

    public static final class AbilityUpgrades {
        public static final String TABLE_NAME = "abilityupgrades";
        public static final String MATCH_ID = "matchid";
        public static final String PLAYER_SLOT = "playerslot";
        public static final String LEVEL = "level";
        public static final String ABILITY = "ability";
        public static final String TIME = "time";
    }

}
