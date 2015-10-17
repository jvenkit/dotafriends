package com.dotafriends.dotafriends.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dotafriends.dotafriends.models.SingleMatchInfo;

import rx.Observable;

/**
 * Database helper singleton responsible for creation and opening of the database. Also provides
 * methods for common CRUD operations in the form of Observables.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance = null;

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "DotaFriendsDatabase.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
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
                    DatabaseContract.MatchInfo.ENGINE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.MatchInfo.ERROR + TEXT_TYPE + " )";

    private static final String SQL_CREATE_PLAYERS_TABLE =
            "CREATE TABLE " + DatabaseContract.Players.TABLE_NAME + " (" +
                    DatabaseContract.Players._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    DatabaseContract.Players.WINS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.Players.LOSSES + INTEGER_TYPE + " )";

    private static final String SQL_CREATE_PLAYER_PERFORMANCE_TABLE =
            "CREATE TABLE " + DatabaseContract.PlayerPerformance.TABLE_NAME + " (" +
                    DatabaseContract.PlayerPerformance.MATCH_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.PLAYER_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.PLAYER_SLOT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.HERO_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_0 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_1 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_2 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_3 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_4 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ITEM_5 + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.KILLS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.DEATHS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.ASSISTS+ INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.LEAVER_STATUS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.GOLD + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.LAST_HITS + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.DENIES + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.GOLD_PER_MIN + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.XP_PER_MIN + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.GOLD_SPENT + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.HERO_DAMAGE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.TOWER_DAMAGE + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.HERO_HEALING + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.PlayerPerformance.LEVEL + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + DatabaseContract.PlayerPerformance.MATCH_ID + ") REFERENCES " +
                    DatabaseContract.MatchInfo.TABLE_NAME + "(" + DatabaseContract.MatchInfo._ID +
                    "))";

    private static final String SQL_CREATE_ABILITY_UPGRADES_TABLE =
            "CREATE TABLE " + DatabaseContract.AbilityUpgrades.TABLE_NAME + " (" +
                    DatabaseContract.AbilityUpgrades.MATCH_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.PLAYER_ID + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.ABILITY + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.TIME + INTEGER_TYPE + COMMA_SEP +
                    DatabaseContract.AbilityUpgrades.LEVEL + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + DatabaseContract.AbilityUpgrades.MATCH_ID + ") REFERENCES " +
                    DatabaseContract.MatchInfo.TABLE_NAME + "(" + DatabaseContract.MatchInfo._ID +
                    "))";

    private static final String SQL_DELETE_MATCH_INFO_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.MatchInfo.TABLE_NAME;
    private static final String SQL_DELETE_PLAYERS_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.Players.TABLE_NAME;
    private static final String SQL_DELETE_PLAYER_PERFORMANCE_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.PlayerPerformance.TABLE_NAME;
    private static final String SQL_DELETE_ABILITY_UPGRADES_TABLE =
            "DROP TABLE IF EXISTS " + DatabaseContract.AbilityUpgrades.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL(SQL_CREATE_PLAYER_PERFORMANCE_TABLE);
        db.execSQL(SQL_CREATE_ABILITY_UPGRADES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MATCH_INFO_TABLE);
        db.execSQL(SQL_DELETE_PLAYERS_TABLE);
        db.execSQL(SQL_DELETE_PLAYER_PERFORMANCE_TABLE);
        db.execSQL(SQL_DELETE_ABILITY_UPGRADES_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Observable<Void> insertMatch(SingleMatchInfo match) {
        return Observable.<Void>create(observer -> {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = getWritableDatabase();

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
                for (SingleMatchInfo.PlayerPerformance player : match.players) {
                    values.put(DatabaseContract.PlayerPerformance.MATCH_ID, match.matchId);
                    values.put(DatabaseContract.PlayerPerformance.PLAYER_ID, player.accountId);
                    values.put(DatabaseContract.PlayerPerformance.PLAYER_SLOT, player.playerSlot);
                    values.put(DatabaseContract.PlayerPerformance.HERO_ID, player.heroId);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_0, player.item0);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_1, player.item1);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_2, player.item2);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_3, player.item3);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_4, player.item4);
                    values.put(DatabaseContract.PlayerPerformance.ITEM_5, player.item5);
                    values.put(DatabaseContract.PlayerPerformance.KILLS, player.kills);
                    values.put(DatabaseContract.PlayerPerformance.DEATHS, player.deaths);
                    values.put(DatabaseContract.PlayerPerformance.ASSISTS, player.assists);
                    values.put(DatabaseContract.PlayerPerformance.LEAVER_STATUS, player.leaverStatus);
                    values.put(DatabaseContract.PlayerPerformance.GOLD, player.gold);
                    values.put(DatabaseContract.PlayerPerformance.LAST_HITS, player.lastHits);
                    values.put(DatabaseContract.PlayerPerformance.DENIES, player.denies);
                    values.put(DatabaseContract.PlayerPerformance.GOLD_PER_MIN, player.goldPerMin);
                    values.put(DatabaseContract.PlayerPerformance.XP_PER_MIN, player.xpPerMin);
                    values.put(DatabaseContract.PlayerPerformance.GOLD_SPENT, player.goldSpent);
                    values.put(DatabaseContract.PlayerPerformance.HERO_DAMAGE, player.heroDamage);
                    values.put(DatabaseContract.PlayerPerformance.TOWER_DAMAGE, player.towerDamage);
                    values.put(DatabaseContract.PlayerPerformance.HERO_HEALING, player.heroHealing);
                    values.put(DatabaseContract.PlayerPerformance.LEVEL, player.level);

                    db.insertOrThrow(DatabaseContract.PlayerPerformance.TABLE_NAME, null, values);

                    if (player.abilityUpgrades != null) {
                        values.clear();
                        for (SingleMatchInfo.PlayerPerformance.AbilityUpgrade ability : player.abilityUpgrades) {
                            values.put(DatabaseContract.AbilityUpgrades.MATCH_ID, match.matchId);
                            values.put(DatabaseContract.AbilityUpgrades.PLAYER_ID, player.accountId);
                            values.put(DatabaseContract.AbilityUpgrades.ABILITY, ability.ability);
                            values.put(DatabaseContract.AbilityUpgrades.TIME, ability.time);
                            values.put(DatabaseContract.AbilityUpgrades.LEVEL, ability.level);

                            db.insertOrThrow(DatabaseContract.AbilityUpgrades.TABLE_NAME,
                                    null, values);
                            values.clear();
                        }
                    }
                }
                db.setTransactionSuccessful();
                observer.onNext(null);
            } catch (SQLException e) {
                observer.onError(e);
            } finally {
                db.endTransaction();
                observer.onCompleted();
            }
        });
    }

    public Observable<Void> deleteMatch(long matchId) {
        return Observable.<Void>create(observer -> {
            String whereClause = DatabaseContract.MatchInfo._ID + " = " + String.valueOf(matchId);
            SQLiteDatabase db = getWritableDatabase();

            db.delete(DatabaseContract.MatchInfo.TABLE_NAME, whereClause, null);

            whereClause = DatabaseContract.PlayerPerformance.MATCH_ID + " = " + String.valueOf(matchId);
            db.delete(DatabaseContract.PlayerPerformance.TABLE_NAME, whereClause, null);

            whereClause = DatabaseContract.AbilityUpgrades.MATCH_ID + " = " + String.valueOf(matchId);
            db.delete(DatabaseContract.AbilityUpgrades.TABLE_NAME, whereClause, null);

            observer.onCompleted();
        });
    }

    public Observable<Void> deleteMatches(long[] matchIds) {
        return Observable.create(observer -> {
            SQLiteDatabase db = getWritableDatabase();

            String[] whereArgs = new String[matchIds.length];
            for (int i = 0; i < matchIds.length; i++) {
                whereArgs[i] = String.valueOf(matchIds[i]);
            }

            String whereClause = DatabaseContract.MatchInfo._ID + " = ?";

            if (!observer.isUnsubscribed()) {
                if (matchIds.length > 1) {
                    StringBuilder sb = new StringBuilder(whereClause);
                    for (int i = 1; i < matchIds.length; i++) {
                        sb.append(" OR " + DatabaseContract.MatchInfo._ID + " = ?");
                    }
                    whereClause = sb.toString();
                }

                db.delete(DatabaseContract.MatchInfo.TABLE_NAME, whereClause, whereArgs);

                whereClause = DatabaseContract.PlayerPerformance.MATCH_ID + " = ?";

                if (matchIds.length > 1) {
                    StringBuilder sb = new StringBuilder(whereClause);
                    for (int i = 1; i < matchIds.length; i++) {
                        sb.append(" OR " + DatabaseContract.PlayerPerformance.MATCH_ID + " = ?");
                    }
                    whereClause = sb.toString();
                }

                db.delete(DatabaseContract.PlayerPerformance.TABLE_NAME, whereClause, whereArgs);

                whereClause = DatabaseContract.AbilityUpgrades.MATCH_ID + " = ?";

                if (matchIds.length > 1) {
                    StringBuilder sb = new StringBuilder(whereClause);
                    for (int i = 1; i < matchIds.length; i++) {
                        sb.append(" OR " + DatabaseContract.AbilityUpgrades.MATCH_ID + " = ?");
                    }
                    whereClause = sb.toString();
                }

                db.delete(DatabaseContract.AbilityUpgrades.TABLE_NAME, whereClause, whereArgs);
            }

            observer.onCompleted();
        });
    }

    public Observable<Void> deleteAllMatches() {
        return Observable.<Void>create(observer -> {
            SQLiteDatabase db = getWritableDatabase();

            db.delete(DatabaseContract.MatchInfo.TABLE_NAME, null, null);
            db.delete(DatabaseContract.AbilityUpgrades.TABLE_NAME, null, null);
            db.delete(DatabaseContract.PlayerPerformance.TABLE_NAME, null, null);

            observer.onCompleted();
        });
    }
}
