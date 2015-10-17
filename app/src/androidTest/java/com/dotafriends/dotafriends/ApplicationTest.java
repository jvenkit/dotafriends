package com.dotafriends.dotafriends;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.dotafriends.dotafriends.services.SteamService;

import rx.schedulers.Schedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String TAG = "AppTest";

    private SteamService mSteamService;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSteamService = new SteamService();
    }

    public void testPreconditions() {
        assertNotNull("mSteamService is null", mSteamService);
    }

    public void testMatchHistoryId() throws InterruptedException {
        Log.d(TAG, "Beginning test");
        mSteamService.fetchMatchIds(52169507, 975640642)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    Log.d(TAG, "Found " + (result.get(result.size() - 1)) + " matches");
                    Log.d(TAG, "First match ID: " + result.get(0));
                    Log.d(TAG, "Last match ID: " + result.get(result.size() - 2));
                });
        Thread.sleep(30000);
    }

    /*public void testRecentMatches() throws InterruptedException {
        Log.d(TAG, "Beginning recent matches test");
        mSteamService.fetchRecentMatches(52169507, 5)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(result -> {
                    Log.d(TAG, "Found a match: " + result.matchId);
                });
        Thread.sleep(15000);
    }*/

}