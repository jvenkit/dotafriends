package com.dotafriends.dotafriends.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.database.DatabaseContract;
import com.dotafriends.dotafriends.database.DatabaseHelper;

/**
 * Fragment that shows the details of a single match
 */
public class MatchDetailFragment extends Fragment {
    private static final String TAG = "DetailFrag";

    public static final String MATCH_ID = "com.dotafriends.dotafriends.match_id";

    private static final String COMMA_SEP = ",";

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
        View v = inflater.inflate(R.layout.fragment_match_detail, container, false);

        TextView matchIdView = (TextView) v.findViewById(R.id.match_id);
        TextView durationView = (TextView) v.findViewById(R.id.duration);
        TextView player1AccView = (TextView) v.findViewById(R.id.player_1_accountid);
        TextView player2GoldView = (TextView) v.findViewById(R.id.player_2_gold);

        mMatchInfoCursor.moveToFirst();
        long matchId = mMatchInfoCursor.getLong(mMatchInfoCursor.getColumnIndexOrThrow(DatabaseContract.MatchInfo._ID));
        int duration = mMatchInfoCursor.getInt(mMatchInfoCursor.getColumnIndexOrThrow(DatabaseContract.MatchInfo.DURATION));
        mPlayerMatchCursor.moveToPosition(0);
        long player1Acc = mPlayerMatchCursor.getLong(mPlayerMatchCursor.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.ACCOUNT_ID));
        mPlayerMatchCursor.moveToPosition(1);
        int player2Gold = mPlayerMatchCursor.getInt(mPlayerMatchCursor.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.GOLD_PER_MIN));

        matchIdView.setText(String.valueOf(matchId));
        durationView.setText(String.valueOf(duration));
        player1AccView.setText(String.valueOf(player1Acc));
        player2GoldView.setText(String.valueOf(player2Gold));

        return v;
    }

    @Override
    public void onDestroy() {
        mMatchInfoCursor.close();
        mPlayerMatchCursor.close();
        mDatabaseHelper.close();
        super.onDestroy();
    }
}
