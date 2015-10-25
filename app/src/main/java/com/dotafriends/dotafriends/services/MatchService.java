package com.dotafriends.dotafriends.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.activities.MainActivity;
import com.dotafriends.dotafriends.database.DatabaseHelper;
import com.dotafriends.dotafriends.fragments.MatchListFragment;
import com.dotafriends.dotafriends.models.SingleMatchInfo;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Service for retrieving match info
 */
public class MatchService extends Service {

    private static final String TAG = "MatchService";
    private static final String WAKE_LOCK_TAG = "MatchService";

    public static final String LATEST_MATCH_EXTRA = "com.dotafriends.dotafriends.latest_match";

    // 0 for initial fetch, 1 for updating matches only, 2 for updating players only
    public static final String FETCH_PARAMS = "com.dotafriends.dotafriends.fetch_params";

    private DatabaseHelper mDbHelper;
    private SteamService mWebService;
    private long mPlayerId;
    private int mProgress;
    private int mProgressMax;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private PowerManager.WakeLock mWakeLock;

    private void broadcastListUpdate() {
        Intent intent = new Intent(MatchListFragment.UPDATE_LIST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void onCreate() {
        mDbHelper = DatabaseHelper.getInstance(this);
        mWebService = new SteamService();
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mPlayerId = getSharedPreferences(MainActivity.SETTINGS, 0)
                .getLong(MainActivity.PLAYER_ID, 0);

        mBuilder = new NotificationCompat.Builder(this);
        Intent activityIntent = new Intent(MatchService.this,
                MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(MatchService.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(activityIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setSmallIcon(R.drawable.hero_icon_default)
                .setContentTitle("DotaFriends")
                .setContentText("Fetching data")
                .setOngoing(true)
                .setAutoCancel(false)
                .setProgress(0, 0, true)
                .setContentIntent(pendingIntent);
        mNotifyManager.notify(0, mBuilder.build());

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG);
        Log.i(TAG, "Acquiring wake lock");
        mWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long latestMatchId = intent.getLongExtra(LATEST_MATCH_EXTRA, 0);
        int fetchParams = intent.getIntExtra(FETCH_PARAMS, 0);
        Observable<Void> serviceObservable;
        switch (fetchParams) {
            case 1:
                serviceObservable = mWebService.fetchMatchIds(mPlayerId, latestMatchId)
                        .flatMap(matchIds -> {
                            mProgressMax = matchIds.size();
                            return Observable.just(matchIds);
                        })
                        .compose(mWebService.fetchMatches())
                        .map(mDbHelper::insertMatch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                break;
            case 2:
                mProgressMax = 0;
                serviceObservable = mWebService.fetchPlayerSummaries(mPlayerId)
                        .map(mDbHelper::insertPlayerSummary)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                break;
            default:
                serviceObservable = mWebService.fetchPlayerSummaries(mPlayerId)
                        .map(mDbHelper::insertPlayerSummary)
                        .last()
                        .flatMap(insertFinished -> mWebService.fetchMatchIds(mPlayerId, latestMatchId))
                        .flatMap(matchIds -> {
                            mProgressMax = matchIds.size();
                            return Observable.just(matchIds);
                        })
                        .compose(mWebService.fetchMatches())
                        .map(mDbHelper::insertMatch)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                break;
        }

        serviceObservable
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Match service finished successfully");
                        mBuilder.setContentText("Data updated")
                                .setProgress(0, 0, false)
                                .setOngoing(false)
                                .setAutoCancel(true);
                        mNotifyManager.notify(0, mBuilder.build());
                        stopSelf();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof SteamService.SteamServiceException) {
                            mBuilder.setContentText(e.getMessage())
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setAutoCancel(true);
                        } else {
                            mBuilder.setContentText("Something went wrong, try again")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setAutoCancel(true);
                        }
                        mNotifyManager.notify(0, mBuilder.build());
                        broadcastListUpdate();
                        stopSelf();
                    }

                    @Override
                    public void onNext(Void v) {
                        if (mProgressMax != 0) {
                            mProgress += 1;
                            mBuilder.setContentText("Adding matches")
                                    .setProgress(mProgressMax, mProgress, false);
                            mNotifyManager.notify(0, mBuilder.build());
                            broadcastListUpdate();
                        }
                    }
                });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Releasing wake lock");
        mWakeLock.release();
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
