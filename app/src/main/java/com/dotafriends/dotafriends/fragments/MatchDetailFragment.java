package com.dotafriends.dotafriends.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.database.DatabaseContract;
import com.dotafriends.dotafriends.database.DatabaseHelper;
import com.dotafriends.dotafriends.helpers.MatchDataFormatter;

/**
 * Fragment that shows the details of a single match
 */
public class MatchDetailFragment extends Fragment {
    private static final String TAG = "DetailFrag";

    public static final String MATCH_ID = "com.dotafriends.dotafriends.match_id";

    private static final String COMMA_SEP = ",";
    private static final Long ANONYMOUS_ID = 4294967295L;

    private Cursor mMatchInfoCursor;
    private Cursor mPlayerMatchCursor;
    private DatabaseHelper mDatabaseHelper;

    private void getCursors(long matchId) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + DatabaseContract.MatchInfo.TABLE_NAME + " WHERE " +
                DatabaseContract.MatchInfo._ID + " = " + matchId;
        mMatchInfoCursor = db.rawQuery(sql, null);

        sql = "SELECT * FROM " + DatabaseContract.PlayerMatchData.TABLE_NAME + " WHERE " +
                DatabaseContract.PlayerMatchData.MATCH_ID + " = " + matchId + " ORDER BY " +
                DatabaseContract.PlayerMatchData.PLAYER_SLOT + " ASC";
        mPlayerMatchCursor = db.rawQuery(sql, null);
    }

    private View generateTableRow(int heroSlot) {
        TableRow tableRow = new TableRow(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.match_detail_table_row, tableRow, false);
        ImageView heroIconView = (ImageView)v.findViewById(R.id.table_row_hero_icon);
        TextView playerNameView = (TextView)v.findViewById(R.id.table_row_player_name);
        TextView levelView = (TextView)v.findViewById(R.id.table_row_level);
        TextView killsView = (TextView)v.findViewById(R.id.table_row_kills);
        TextView deathsView = (TextView)v.findViewById(R.id.table_row_deaths);
        TextView assistsView = (TextView)v.findViewById(R.id.table_row_assists);
        TextView goldView = (TextView)v.findViewById(R.id.table_row_gold);
        TextView lastHitsView = (TextView)v.findViewById(R.id.table_row_last_hits);
        TextView deniesView = (TextView)v.findViewById(R.id.table_row_denies);
        TextView xpPerMinView = (TextView)v.findViewById(R.id.table_row_xp_per_min);
        TextView goldPerMinView = (TextView)v.findViewById(R.id.table_row_gold_per_min);
        TextView heroDamageView = (TextView)v.findViewById(R.id.table_row_hero_damage);
        TextView heroHealingView = (TextView)v.findViewById(R.id.table_row_hero_healing);
        TextView towerDamageView = (TextView)v.findViewById(R.id.table_row_tower_damage);

        mPlayerMatchCursor.moveToPosition(heroSlot);
        int heroId = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.HERO_ID));
        long accountId = mPlayerMatchCursor.getLong(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.ACCOUNT_ID));
        int level = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.LEVEL));
        int kills = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.KILLS));
        int deaths = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.DEATHS));
        int assists = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.ASSISTS));
        int gold = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.GOLD));
        int lastHits = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.LAST_HITS));
        int denies = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.DENIES));
        int xpPerMin = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.XP_PER_MIN));
        int goldPerMin = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.GOLD_PER_MIN));
        int heroDamage = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.HERO_DAMAGE));
        int heroHealing = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.HERO_HEALING));
        int towerDamage = mPlayerMatchCursor.getInt(mPlayerMatchCursor
                .getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.TOWER_DAMAGE));

        heroIconView.setImageResource(MatchDataFormatter.getHeroIconDrawable(heroId));
        playerNameView.setText(accountId == ANONYMOUS_ID ? "Anonymous" : String.valueOf(accountId));
        levelView.setText(String.valueOf(level));
        killsView.setText(String.valueOf(kills));
        deathsView.setText(String.valueOf(deaths));
        assistsView.setText(String.valueOf(assists));
        goldView.setText(String.valueOf(gold));
        lastHitsView.setText(String.valueOf(lastHits));
        deniesView.setText(String.valueOf(denies));
        xpPerMinView.setText(String.valueOf(xpPerMin));
        goldPerMinView.setText(String.valueOf(goldPerMin));
        heroDamageView.setText(String.valueOf(heroDamage));
        heroHealingView.setText(String.valueOf(heroHealing));
        towerDamageView.setText(String.valueOf(towerDamage));

        return v;
    }

    public static MatchDetailFragment newInstance(long matchId) {
        Log.d(TAG, "Create new detail fragment");

        Bundle args = new Bundle();
        args.putLong(MATCH_ID, matchId);

        MatchDetailFragment fragment = new MatchDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDatabaseHelper == null)
            mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        getCursors(getArguments().getLong(MATCH_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_detail, container, false);
        TableLayout radiantTable = (TableLayout)view.findViewById(R.id.radiant_table);
        TableLayout direTable = (TableLayout)view.findViewById(R.id.dire_table);
        inflater.inflate(R.layout.match_detail_table_header, radiantTable, true);
        inflater.inflate(R.layout.match_detail_table_header, direTable, true);

        for (int i = 0; i < 5; i++) {
            radiantTable.addView(generateTableRow(i));
        }

        for (int i = 5; i < 10; i++) {
            direTable.addView(generateTableRow(i));
        }

        return view;
    }

    @Override
    public void onDestroy() {
        mMatchInfoCursor.close();
        mPlayerMatchCursor.close();
        mDatabaseHelper.close();
        super.onDestroy();
    }
}
