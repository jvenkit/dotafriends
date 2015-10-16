package com.dotafriends.dotafriends.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for holding a list of match IDs deserialised from JSON objects fetched using the
 * Dota 2 Web API. Should only be used to temporarily hold match IDs.
 */
public class MatchHistory extends WebApiResult {
    public int numResults;
    public int totalResults;
    public int resultsRemaining;
    public List<MatchHistoryMatch> matches = new ArrayList<>();

    public class MatchHistoryMatch {
        public long matchId;
    }
}
