package com.dotafriends.dotafriends;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.dotafriends.dotafriends.helpers.DataFormatter;
import com.dotafriends.dotafriends.models.PlayerSummary;
import com.dotafriends.dotafriends.services.SteamService;

import rx.schedulers.Schedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String TAG = "dotatest";

    private SteamService mSteamService;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSteamService = new SteamService();
    }

    public void testSteamIdConversion() {
        long number = 76561198012435235L;
        String expected = String.valueOf(number);
        String actual = DataFormatter.get64BitSteamId(52169507);
        assertEquals(expected, actual);

        number = 76561197987050973L;
        expected = String.valueOf(number);
        actual = DataFormatter.get64BitSteamId(26785245);
        assertEquals(expected, actual);

        long expected2 = 52169507L;
        long actual2 = DataFormatter.getAccountId("76561198012435235");
        assertEquals(expected2, actual2);

        expected2 = 26785245L;
        actual2 = DataFormatter.getAccountId("76561197987050973");
        assertEquals(expected2, actual2);
    }

    public void testPlayerSummaries() throws InterruptedException {
        mSteamService.fetchPlayerSummaries(52169507L)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(playerSummary -> {
                    Log.d(TAG, "Got player name: " + playerSummary.getPersonaName());
                });

        Thread.sleep(60000);
    }
}