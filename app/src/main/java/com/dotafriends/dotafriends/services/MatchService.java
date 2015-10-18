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
    public static final String LIST_UPDATED = "com.dotafriends.dotafriends.list_updated";

    private DatabaseHelper mDbHelper;
    private SteamService mWebService;
    private long mPlayerId;
    private int mProgress;
    private int mProgressMax;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private PowerManager.WakeLock mWakeLock;

    private void broadcastListUpdate() {
        Intent intent = new Intent(LIST_UPDATED);
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
                .setContentText("Getting list of matches")
                .setOngoing(true)
                .setAutoCancel(false)
                .setProgress(0, 0, true)
                .setContentIntent(pendingIntent);
        mNotifyManager.notify(0, mBuilder.build());

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                WAKE_LOCK_TAG);
        mWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long latestMatchId = intent.getLongExtra(LATEST_MATCH_EXTRA, 0);
        mWebService.fetchMatchIds(mPlayerId, latestMatchId)
                .flatMap(matchIds -> {
                    mProgressMax = matchIds.size();
                    return Observable.just(matchIds);
                })
                .compose(mWebService.fetchMatches())
                .flatMap(mDbHelper::insertMatch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SingleMatchInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Finished");
                        mBuilder.setContentText("Finished adding matches")
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
                            mBuilder.setContentText("Error adding matches")
                                    .setProgress(0, 0, false)
                                    .setOngoing(false)
                                    .setAutoCancel(true);
                        }
                        mNotifyManager.notify(0, mBuilder.build());
                        broadcastListUpdate();
                        stopSelf();
                    }

                    @Override
                    public void onNext(SingleMatchInfo match) {
                        mProgress += 1;
                        mBuilder.setContentText("Adding match : " + match.matchId)
                                .setProgress(mProgressMax, mProgress, false);
                        mNotifyManager.notify(0, mBuilder.build());
                        broadcastListUpdate();
                    }
                });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mWakeLock.release();
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
