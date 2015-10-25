package com.dotafriends.dotafriends.services;

import android.util.Log;

import com.dotafriends.dotafriends.helpers.DataFormatter;
import com.dotafriends.dotafriends.models.DotaApiResult;
import com.dotafriends.dotafriends.models.MatchHistory;
import com.dotafriends.dotafriends.models.MatchHistoryMatch;
import com.dotafriends.dotafriends.models.Player;
import com.dotafriends.dotafriends.models.PlayerSummaries;
import com.dotafriends.dotafriends.models.PlayerSummary;
import com.dotafriends.dotafriends.models.SingleMatchInfo;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Class responsible for making API requests using Retrofit. Methods return Observables that emit
 * the relevant data.
 */
public class SteamService {
    private static final String TAG = "dotaservice";

    private static final String API_KEY = "4E558323C263A62086A224D9A0E3A9A1";
    private static final String WEB_SERVICE_BASE_URL = "https://api.steampowered.com";

    private final SteamWebService mWebService;

    public SteamService() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEB_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mWebService = retrofit.create(SteamWebService.class);
    }

    private interface SteamWebService {
        @GET("/IDOTA2Match_570/GetMatchDetails/v001/")
        Observable<DotaEnvelope<SingleMatchInfo>> fetchMatchDetails(
                @Query("key") String key,
                @Query("match_id") long matchId
        );

        @GET("/IDOTA2Match_570/GetMatchHistory/v001/")
        Observable<DotaEnvelope<MatchHistory>> fetchMatchHistory(
                @Query("key") String key,
                @Query("account_id") long accountId,
                @Query("start_at_match_id") long startAtMatchId,
                @Query("matches_requested") int matchesRequested
        );

        @GET("/ISteamUser/GetPlayerSummaries/v0002/")
        Observable<SteamEnvelope<PlayerSummaries>> fetchPlayerSummaries(
                @Query("key") String key,
                @Query("steamids") String steamIds
        );
    }

    public Observable<List<Long>> fetchMatchIds(long accountId, long latestMatchId) {
        Log.d(TAG, "Fetching match");
        return fetchAllMatchIds(accountId)
                .flatMap(matchIds -> {
                    int index = matchIds.indexOf(latestMatchId);
                    if (index >= 0)
                        matchIds.subList(matchIds.indexOf(latestMatchId), matchIds.size()).clear();
                    return Observable.just(matchIds);
                });
    }

    public Observable.Transformer<List<Long>, SingleMatchInfo> fetchMatches() {
        Log.d(TAG, "Fetching matches");
        return matchIdsObservable ->
            matchIdsObservable
                    .flatMap(matchIds -> Observable.interval(2000, 2000, TimeUnit.MILLISECONDS)
                            .take(matchIds.size())
                            .flatMap(tick -> fetchMatchDetails(
                                            matchIds.get(matchIds.size() - 1 - tick.intValue()))
                            )
                    );
    }

    public Observable<PlayerSummary> fetchPlayerSummaries(long accountId) {
        return fetchAccountIds(accountId)
                .flatMap(accountIds -> {
                    StringBuilder sb = new StringBuilder();
                    if (!accountIds.isEmpty()) {
                        for (int i = 0; i < accountIds.size(); i++) {
                            sb.append(DataFormatter.get64BitSteamId(accountIds.get(i)));
                            if (i < accountIds.size() - 1) sb.append(",");
                        }
                        return mWebService.fetchPlayerSummaries(API_KEY, sb.toString())
                                .flatMap(steamEnvelope -> {
                                    if (steamEnvelope.response != null) {
                                        return Observable.from(steamEnvelope.response.getPlayers());
                                    } else {
                                        return Observable.error(new SteamServiceException("Bad call to fetchPlayerSummaries"));
                                    }
                                });
                    } else {
                        return Observable.empty();
                    }
                });
    }

    private Observable<SingleMatchInfo> fetchMatchDetails(long matchId) {
        return mWebService.fetchMatchDetails(API_KEY, matchId)
                .retry(3)
                .compose(this.<SingleMatchInfo>filterWebErrors());
    }

    private Observable<MatchHistory> fetchMatchHistoryRecursive(MatchHistory requestData, long accountId) {
        if (requestData.getResultsRemaining() > 0) {
            return Observable.concat(Observable.just(requestData),
                    mWebService.fetchMatchHistory(API_KEY, accountId,
                            requestData.getMatches().get(requestData.getNumResults() - 1).getMatchId(), 100)
                            .compose(this.<MatchHistory>filterWebErrors())
                            .delay(2000, TimeUnit.MILLISECONDS)
                            .flatMap(result -> fetchMatchHistoryRecursive(result, accountId))
            );
        } else {
            return Observable.just(requestData);
        }
    }

    private Observable<List<Long>> fetchAllMatchIds(long accountId) {
        List<Long> matchIds = new ArrayList<>();

        return mWebService.fetchMatchHistory(API_KEY, accountId, 0, 100)
                .retry(3)
                .compose(this.<MatchHistory>filterWebErrors())
                .flatMap(result -> fetchMatchHistoryRecursive(result, accountId))
                .flatMap(matchHistory -> {
                    if (matchIds.size() == 0) {
                        for (MatchHistoryMatch match : matchHistory.getMatches()) {
                            matchIds.add(match.getMatchId());
                        }
                        return Observable.just(matchIds);
                    } else {
                        for (int i = 1; i < matchHistory.getMatches().size(); i++) {
                            matchIds.add(matchHistory.getMatches().get(i).getMatchId());
                        }
                        return Observable.just(matchIds);
                    }
                })
                .last();
    }

    private Observable<List<Long>> fetchAccountIds(long accountId) {
        List<Long> players = new ArrayList<>();

        return mWebService.fetchMatchHistory(API_KEY, accountId, 0, 100)
                .retry(3)
                .compose(this.<MatchHistory>filterWebErrors())
                .flatMap(result -> fetchMatchHistoryRecursive(result, accountId))
                .flatMap(matchHistory -> {
                    if (players.size() == 0) {
                        for (MatchHistoryMatch match : matchHistory.getMatches()) {
                            for (Player player : match.getPlayers()) {
                                if (!players.contains(player.getAccountId()))
                                    players.add(player.getAccountId());
                            }
                        }
                        return Observable.just(players);
                    } else {
                        for (int i = 1; i < matchHistory.getMatches().size(); i++) {
                            for (Player player : matchHistory.getMatches().get(i).getPlayers()) {
                                if (!players.contains(player.getAccountId()))
                                    players.add(player.getAccountId());
                            }
                        }
                        return Observable.just(players);
                    }
                })
                .last()
                // Split account IDs into lots of 100, use interval to limit API calls
                .flatMap(accountIds -> {
                    Log.d(TAG, "Found " + accountIds.size() + " IDs");
                    if (accountIds.size() <= 100) {
                        return Observable.just(accountIds);
                    } else {
                        int split = accountIds.size() / 100;
                        return Observable.interval(2000, TimeUnit.MILLISECONDS)
                                .take(split + 1)
                                .flatMap(tick -> {
                                    if (tick != split) {
                                        return Observable.just(accountIds.subList(
                                                        tick.intValue() * 100, tick.intValue() * 100 + 100)
                                        );
                                    } else {
                                        return Observable.just(accountIds.subList(
                                                        tick.intValue() * 100, accountIds.size())
                                        );
                                    }
                                });
                    }
                });
    }

    private class DotaEnvelope<T extends DotaApiResult> {
        private T result;
    }

    private class SteamEnvelope<T> {
        private T response;
    }

    private <T extends DotaApiResult> Observable.Transformer<DotaEnvelope<T>, T> filterWebErrors() {
        return dataEnvelopeObservable ->
                dataEnvelopeObservable
                        .flatMap(requestData -> {
                            if (requestData.result == null) {
                                return Observable.error(new SteamServiceException("Steam Web API is not responding, try again later"));
                            } else if (requestData.result.getError() != null) {
                                return Observable.error(new SteamServiceException(requestData.result.getError()));
                            } else if (requestData.result.getStatusDetail() != null) {
                                return Observable.error(new SteamServiceException(requestData.result.getStatusDetail()));
                            } else {
                                return Observable.just(requestData.result);
                            }
                        });
    }

    public class SteamServiceException extends Exception {
        SteamServiceException(String detailMessage) { super(detailMessage); }
    }
}
