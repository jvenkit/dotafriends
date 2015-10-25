package com.dotafriends.dotafriends.fragments;

import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.activities.MainActivity;
import com.dotafriends.dotafriends.adapters.MatchListAdapter;
import com.dotafriends.dotafriends.database.DatabaseContract;
import com.dotafriends.dotafriends.database.DatabaseHelper;
import com.dotafriends.dotafriends.services.MatchService;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Fragment that displays a list of matches.
 */
public class MatchListFragment extends ListFragment
        implements DialogPlayerFragment.DialogPlayerListener {

    private static final String TAG = "MatchListFragment";

    public static final String UPDATE_LIST = "com.dotafriends.dotafriends.update_list";

    private static final String COMMA_SEP = ",";

    private MatchListAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;
    private String mListQuery;
    private Observable<Cursor> mListObservable;
    private Listener mListener;

    private BroadcastReceiver mListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateListView();
        }
    };

    public interface Listener {
        void onSelectMatch(long matchId);
    }

    public MatchListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mDatabaseHelper == null)
            mDatabaseHelper = DatabaseHelper.getInstance(getActivity());
        rebuildListQuery();
        mListObservable = Observable.<Cursor>create(observer -> {
            Cursor cursor = mDatabaseHelper.getReadableDatabase()
                    .rawQuery(mListQuery, null);
            observer.onNext(cursor);
            observer.onCompleted();
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.empty_list));
        updateListView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onSelectMatch(id);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mListReceiver,
                new IntentFilter(UPDATE_LIST));
        mListener = (Listener) getActivity();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mListReceiver);
        mListener = null;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_match_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update_matches:
                Intent intent = new Intent(getActivity(), MatchService.class);
                intent.putExtra(MatchService.LATEST_MATCH_EXTRA,
                        getListAdapter().getItemId(0));
                // Only update matches
                intent.putExtra(MatchService.FETCH_PARAMS, 1);
                getActivity().startService(intent);
                return true;
            case R.id.action_track_player:
                DialogFragment dialog = new DialogPlayerFragment();
                dialog.show(getFragmentManager(), "addPlayer");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogAddPlayer(DialogFragment dialog, long playerId) {
        mDatabaseHelper.deleteAllMatches()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        SharedPreferences settings = getActivity()
                                .getSharedPreferences(MainActivity.SETTINGS, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putLong(MainActivity.PLAYER_ID, playerId);
                        editor.apply();
                        rebuildListQuery();
                        updateListView();
                        Intent intent = new Intent(getActivity(), MatchService.class);
                        // Use 0 for initial fetch
                        intent.putExtra(MatchService.FETCH_PARAMS, 0);
                        getActivity().startService(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    private void rebuildListQuery() {
        mListQuery = "SELECT " + DatabaseContract.MatchInfo.TABLE_NAME + "." +
                DatabaseContract.MatchInfo._ID + COMMA_SEP +
                DatabaseContract.MatchInfo.DURATION + COMMA_SEP +
                DatabaseContract.MatchInfo.RADIANT_WIN + COMMA_SEP +
                DatabaseContract.MatchInfo.GAME_MODE + COMMA_SEP +
                DatabaseContract.MatchInfo.START_TIME + COMMA_SEP +
                DatabaseContract.PlayerMatchData.PLAYER_SLOT + COMMA_SEP +
                DatabaseContract.PlayerMatchData.HERO_ID + COMMA_SEP +
                DatabaseContract.PlayerMatchData.KILLS + COMMA_SEP +
                DatabaseContract.PlayerMatchData.DEATHS + COMMA_SEP +
                DatabaseContract.PlayerMatchData.ASSISTS + " FROM " +
                DatabaseContract.MatchInfo.TABLE_NAME + " INNER JOIN " +
                DatabaseContract.PlayerMatchData.TABLE_NAME + " ON " +
                DatabaseContract.MatchInfo._ID + "=" +
                DatabaseContract.PlayerMatchData.MATCH_ID + " WHERE " +
                DatabaseContract.PlayerMatchData.ACCOUNT_ID + "=" +
                getActivity().getSharedPreferences(MainActivity.SETTINGS, 0)
                        .getLong(MainActivity.PLAYER_ID, 0)
                + " ORDER BY " + DatabaseContract.MatchInfo._ID + " DESC";
    }

    private void updateListView() {
        mListObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cursor -> {
                    if (mAdapter == null) {
                        mAdapter = new MatchListAdapter(getActivity(), cursor, 0);
                        setListAdapter(mAdapter);
                    } else {
                        mAdapter.changeCursor(cursor);
                    }
                });
    }
}
