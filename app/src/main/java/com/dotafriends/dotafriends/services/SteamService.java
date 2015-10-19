package com.dotafriends.dotafriends.services;

import com.dotafriends.dotafriends.models.MatchHistory;
import com.dotafriends.dotafriends.models.MatchHistoryMatch;
import com.dotafriends.dotafriends.models.PlayerSummaries;
import com.dotafriends.dotafriends.models.SingleMatchInfo;
import com.dotafriends.dotafriends.models.DotaApiResult;
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
    private static final String TAG = "SteamService";

    private static final String API_KEY = "4E558323C263A62086A224D9A0E3A9A1";
    private static final String WEB_SERVICE_BASE_URL = "https://api.steampowered.com";

    private final DotaMatchService mWebService;

    public SteamService() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEB_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mWebService = retrofit.create(DotaMatchService.class);
    }

    private interface DotaMatchService {
        @GET("/IDOTA2Match_570/GetMatchDetails/v001/")
        Observable<DotaEnvelop<SingleMatchInfo>> fetchMatchDetails(
                @Query("key") String key,
                @Query("match_id") long matchId
        );

        @GET("/IDOTA2Match_570/GetMatchHistory/v001/")
        Observable<DotaEnvelop<MatchHistory>> fetchMatchHistory(
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

    private class DotaEnvelop<T extends DotaApiResult> {
        private T result;
    }

    private class SteamEnvelope<T> {
        private T response;
    }

    public class SteamServiceException extends Exception {
        SteamServiceException(String detailMessage) { super(detailMessage); }
    }

    public Observable<List<Long>> fetchMatchIds(long accountId, long latestMatchId) {
        return fetchAllMatchIds(accountId)
                .flatMap(matchIds -> {
                    int index = matchIds.indexOf(latestMatchId);
                    if (index >= 0)
                        matchIds.subList(matchIds.indexOf(latestMatchId), matchIds.size()).clear();
                    return Observable.just(matchIds);
                });
    }

    public Observable.Transformer<List<Long>, SingleMatchInfo> fetchMatches() {
        return matchIdsObservable ->
            matchIdsObservable
                    .flatMap(matchIds -> Observable.interval(2000, 2000, TimeUnit.MILLISECONDS)
                            .take(matchIds.size())
                            .flatMap(tick -> fetchMatchDetails(
                                            matchIds.get(matchIds.size() - 1 - tick.intValue()))
                            )
                    );
    }

    private Observable<SingleMatchInfo> fetchMatchDetails(long matchId) {
        return mWebService.fetchMatchDetails(API_KEY, matchId)
                .retry(3)
                .compose(this.<SingleMatchInfo>filterWebErrors());
    }

    private Observable<MatchHistory> fetchMatchHistory(long accountId) {
        return mWebService.fetchMatchHistory(API_KEY, accountId, 0, 100)
                .retry(3)
                .compose(this.<MatchHistory>filterWebErrors());
    }

    private Observable<MatchHistory> fetchRemainingMatchHistory(MatchHistory requestData, long accountId) {
        if (requestData.getResultsRemaining() > 0) {
            return Observable.concat(Observable.just(requestData),
                    mWebService.fetchMatchHistory(API_KEY, accountId,
                            requestData.getMatches().get(requestData.getNumResults() - 1).getMatchId(), 100)
                            .compose(this.<MatchHistory>filterWebErrors())
                            .delay(2000, TimeUnit.MILLISECONDS)
                            .flatMap(result -> fetchRemainingMatchHistory(result, accountId))
            );
        } else {
            return Observable.just(requestData);
        }
    }

    private Observable<List<Long>> fetchAllMatchIds(long accountId) {
        List<Long> matchIds = new ArrayList<>();

        return fetchMatchHistory(accountId)
                .flatMap(result -> fetchRemainingMatchHistory(result, accountId))
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

    private <T extends DotaApiResult> Observable.Transformer<DotaEnvelop<T>, T> filterWebErrors() {
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
}
