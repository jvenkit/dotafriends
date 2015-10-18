package com.dotafriends.dotafriends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dotafriends.dotafriends.activities.MainActivity;
import com.dotafriends.dotafriends.helpers.MatchDataFormatter;
import com.dotafriends.dotafriends.models.SingleMatchInfo;

import rx.Observable;

/**
 * Database helper singleton responsible for creation and opening of the database. Also provides
 * methods for common CRUD operations in the form of Observables.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance = null;

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "DotaFriendsDatabase.db";

    private static final long ANONYMOUS_ID = 4294967295L;

    private Context mContext;

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String ZERO_INTEGER_TYPE = " INTEGER DEFAULT 0";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_MATCH_INFO_TABLE =
            "CREATE TABLE " + DatabaseContract.MatchInfo.TABLE_NAME + " (" +
                    DatabaseContract.MatchInfo._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    DatabaseContract.MatchInfo.RADIANT_WIN + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.DURATION + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.START_TIME + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.MATCH_SEQ_NUM + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.TOWER_STATUS_RADIANT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.TOWER_STATUS_DIRE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.CLUSTER + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.FIRST_BLOOD_TIME + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.LOBBY_TYPE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.HUMAN_PLAYERS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.LEAGUE_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.POSITIVE_VOTES + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.NEGATIVE_VOTES + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.GAME_MODE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.ENGINE + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_PLAYERS_TABLE =
            "CREATE TABLE " + DatabaseContract.Players.TABLE_NAME + " (" +
                    DatabaseContract.Players.ACCOUNT_ID + INTEGER_TYPE + " UNIQUE" + COMMA_SEP +
                    DatabaseContract.Players.WINS_WITH + ZERO_INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Players.LOSSES_WITH + ZERO_INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Players.WINS_AGAINST + ZERO_INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Players.LOSSES_AGAINST + ZERO_INTEGER_TYPE + " )";

    private static final String SQL_CREATE_PLAYER_MATCH_DATA_TABLE =
            "CREATE TABLE " + DatabaseContract.PlayerMatchData.TABLE_NAME + " (" +
                    DatabaseContract.PlayerMatchData.MATCH_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.PLAYER_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.PLAYER_SLOT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.HERO_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_0 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_1 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_2 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_3 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_4 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ITEM_5 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.KILLS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.DEATHS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.ASSISTS+ INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.LEAVER_STATUS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.GOLD + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.LAST_HITS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.DENIES + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.GOLD_PER_MIN + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.XP_PER_MIN + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.GOLD_SPENT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.HERO_DAMAGE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.TOWER_DAMAGE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.HERO_HEALING + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.LEVEL + INTEGER_TYPE + COMMA_SEP +
                    "PRIMARY KEY (" + DatabaseContract.PlayerMatchData.MATCH_ID + COMMA_SEP +
                    DatabaseContract.PlayerMatchData.PLAYER_SLOT + " )" + COMMA_SEP +
                    "FOREIGN KEY (" + DatabaseContract.PlayerMatchData.MATCH_ID + ") REFERENCES " +
                    DatabaseContract.MatchInfo.TABLE_NAME + "(" + DatabaseContract.MatchInfo._ID +
                    "))";

    private static final String SQL_CREATE_ABILITY_UPGRADES_TABLE =
            "CREATE TABLE " + DatabaseContract.AbilityUpgrades.TABLE_NAME + " (" +
                    DatabaseContract.AbilityUpgrades.MATCH_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.PLAYER_SLOT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.LEVEL + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.ABILITY + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.TIME + INTEGER_TYPE + COMMA_SEP +
                    "PRIMARY KEY (" + DatabaseContract.AbilityUpgrades.MATCH_ID + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.PLAYER_SLOT + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.LEVEL + ")" + COMMA_SEP +
                    "FOREIGN KEY (" + DatabaseContract.AbilityUpgrades.MATCH_ID + ") REFERENCES " +
                    DatabaseContract.MatchInfo.TABLE_NAME + "(" + DatabaseContract.MatchInfo._ID +
                    "))";

    private static final String SQL_DELETE_MATCH_INFO_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.MatchInfo.TABLE_NAME;
    private static final String SQL_DELETE_PLAYERS_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Players.TABLE_NAME;
    private static final String SQL_DELETE_PLAYER_MATCH_DATA_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.PlayerMatchData.TABLE_NAME;
    private static final String SQL_DELETE_ABILITY_UPGRADES_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.AbilityUpgrades.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            return new DatabaseHelper(context);
        } else {
            return sInstance;
        }
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MATCH_INFO_TABLE);
        db.execSQL(SQL_CREATE_PLAYERS_TABLE);
        db.execSQL(SQL_CREATE_PLAYER_MATCH_DATA_TABLE);
        db.execSQL(SQL_CREATE_ABILITY_UPGRADES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MATCH_INFO_TABLE);
        db.execSQL(SQL_DELETE_PLAYERS_TABLE);
        db.execSQL(SQL_DELETE_PLAYER_MATCH_DATA_TABLE);
        db.execSQL(SQL_DELETE_ABILITY_UPGRADES_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Observable<SingleMatchInfo> insertMatch(SingleMatchInfo match) {
        return Observable.<SingleMatchInfo>create(observer -> {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = getWritableDatabase();
            long userId = mContext.getSharedPreferences(MainActivity.SETTINGS, 0)
                    .getLong(MainActivity.PLAYER_ID, 0);
            int userSlot = 0;
            for (SingleMatchInfo.PlayerMatchData player : match.players) {
                if (player.accountId == userId)
                    userSlot = player.playerSlot;
            }

            db.beginTransaction();
            try {
                values.put(DatabaseContract.MatchInfo._ID, match.matchId);
                values.put(DatabaseContract.MatchInfo.RADIANT_WIN, match.radiantWin);
                values.put(DatabaseContract.MatchInfo.DURATION, match.duration);
                values.put(DatabaseContract.MatchInfo.START_TIME, match.startTime);
                values.put(DatabaseContract.MatchInfo.MATCH_SEQ_NUM, match.matchSeqNum);
                values.put(DatabaseContract.MatchInfo.TOWER_STATUS_RADIANT, match.towerStatusRadiant);
                values.put(DatabaseContract.MatchInfo.TOWER_STATUS_DIRE, match.towerStatusDire);
                values.put(DatabaseContract.MatchInfo.CLUSTER, match.cluster);
                values.put(DatabaseContract.MatchInfo.FIRST_BLOOD_TIME, match.firstBloodTime);
                values.put(DatabaseContract.MatchInfo.LOBBY_TYPE, match.lobbyType);
                values.put(DatabaseContract.MatchInfo.LEAGUE_ID, match.leagueId);
                values.put(DatabaseContract.MatchInfo.POSITIVE_VOTES, match.positiveVotes);
                values.put(DatabaseContract.MatchInfo.NEGATIVE_VOTES, match.negativeVotes);
                values.put(DatabaseContract.MatchInfo.GAME_MODE, match.gameMode);
                values.put(DatabaseContract.MatchInfo.ENGINE, match.engine);

                db.insertOrThrow(DatabaseContract.MatchInfo.TABLE_NAME, null, values);

                values.clear();
                for (SingleMatchInfo.PlayerMatchData player : match.players) {
                    values.put(DatabaseContract.PlayerMatchData.MATCH_ID, match.matchId);
                    values.put(DatabaseContract.PlayerMatchData.PLAYER_ID, player.accountId);
                    values.put(DatabaseContract.PlayerMatchData.PLAYER_SLOT, player.playerSlot);
                    values.put(DatabaseContract.PlayerMatchData.HERO_ID, player.heroId);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_0, player.item0);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_1, player.item1);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_2, player.item2);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_3, player.item3);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_4, player.item4);
                    values.put(DatabaseContract.PlayerMatchData.ITEM_5, player.item5);
                    values.put(DatabaseContract.PlayerMatchData.KILLS, player.kills);
                    values.put(DatabaseContract.PlayerMatchData.DEATHS, player.deaths);
                    values.put(DatabaseContract.PlayerMatchData.ASSISTS, player.assists);
                    values.put(DatabaseContract.PlayerMatchData.LEAVER_STATUS, player.leaverStatus);
                    values.put(DatabaseContract.PlayerMatchData.GOLD, player.gold);
                    values.put(DatabaseContract.PlayerMatchData.LAST_HITS, player.lastHits);
                    values.put(DatabaseContract.PlayerMatchData.DENIES, player.denies);
                    values.put(DatabaseContract.PlayerMatchData.GOLD_PER_MIN, player.goldPerMin);
                    values.put(DatabaseContract.PlayerMatchData.XP_PER_MIN, player.xpPerMin);
                    values.put(DatabaseContract.PlayerMatchData.GOLD_SPENT, player.goldSpent);
                    values.put(DatabaseContract.PlayerMatchData.HERO_DAMAGE, player.heroDamage);
                    values.put(DatabaseContract.PlayerMatchData.TOWER_DAMAGE, player.towerDamage);
                    values.put(DatabaseContract.PlayerMatchData.HERO_HEALING, player.heroHealing);
                    values.put(DatabaseContract.PlayerMatchData.LEVEL, player.level);

                    db.insertOrThrow(DatabaseContract.PlayerMatchData.TABLE_NAME, null, values);

                    if (player.abilityUpgrades != null) {
                        values.clear();
                        for (SingleMatchInfo.PlayerMatchData.AbilityUpgrade ability : player.abilityUpgrades) {
                            values.put(DatabaseContract.AbilityUpgrades.MATCH_ID, match.matchId);
                            values.put(DatabaseContract.AbilityUpgrades.PLAYER_SLOT, player.playerSlot);
                            values.put(DatabaseContract.AbilityUpgrades.ABILITY, ability.ability);
                            values.put(DatabaseContract.AbilityUpgrades.TIME, ability.time);
                            values.put(DatabaseContract.AbilityUpgrades.LEVEL, ability.level);

                            db.insertOrThrow(DatabaseContract.AbilityUpgrades.TABLE_NAME,
                                    null, values);
                            values.clear();
                        }
                    }

                    if (player.accountId != userId && player.accountId != ANONYMOUS_ID) {
                        db.execSQL("INSERT OR IGNORE INTO " + DatabaseContract.Players.TABLE_NAME +
                                " (" + DatabaseContract.Players.ACCOUNT_ID + ") VALUES (" +
                                player.accountId + ")"
                        );

                        if ((userSlot < 5 && player.playerSlot < 5) ||
                                (userSlot > 5 && player.playerSlot > 5)) {
                            if (MatchDataFormatter.isWin(userSlot, match.radiantWin ? 1 : 0)) {
                                db.execSQL("UPDATE " + DatabaseContract.Players.TABLE_NAME +
                                        " SET " + DatabaseContract.Players.WINS_WITH + " = " +
                                        DatabaseContract.Players.WINS_WITH + " + 1 WHERE " +
                                        DatabaseContract.Players.ACCOUNT_ID + " = "
                                        + player.accountId
                                );
                            } else {
                                db.execSQL("UPDATE " + DatabaseContract.Players.TABLE_NAME +
                                                " SET " + DatabaseContract.Players.LOSSES_WITH +
                                                " = " + DatabaseContract.Players.LOSSES_WITH +
                                                " + 1 WHERE " +
                                                DatabaseContract.Players.ACCOUNT_ID + " = " +
                                                player.accountId
                                );
                            }
                        } else {
                            if (MatchDataFormatter.isWin(userSlot, match.radiantWin ? 1 : 0)) {
                                db.execSQL("UPDATE " + DatabaseContract.Players.TABLE_NAME +
                                                " SET " + DatabaseContract.Players.WINS_AGAINST +
                                                " = " + DatabaseContract.Players.WINS_AGAINST +
                                                " + 1 WHERE " +
                                                DatabaseContract.Players.ACCOUNT_ID + " = " +
                                                player.accountId
                                );
                            } else {
                                db.execSQL("UPDATE " + DatabaseContract.Players.TABLE_NAME +
                                                " SET " + DatabaseContract.Players.LOSSES_AGAINST +
                                                " = " + DatabaseContract.Players.LOSSES_AGAINST +
                                                " + 1 WHERE " +
                                                DatabaseContract.Players.ACCOUNT_ID + " = " +
                                                player.accountId
                                );
                            }
                        }
                    }
                }
                db.setTransactionSuccessful();
                observer.onNext(match);
            } catch (SQLException e) {
                observer.onError(e);
            } finally {
                db.endTransaction();
                observer.onCompleted();
            }
        });
    }

    public Observable<Void> deleteAllMatches() {
        return Observable.<Void>create(observer -> {
            SQLiteDatabase db = getWritableDatabase();

            db.delete(DatabaseContract.MatchInfo.TABLE_NAME, null, null);
            db.delete(DatabaseContract.AbilityUpgrades.TABLE_NAME, null, null);
            db.delete(DatabaseContract.PlayerMatchData.TABLE_NAME, null, null);
            db.delete(DatabaseContract.Players.TABLE_NAME, null, null);

            observer.onCompleted();
        });
    }
}
