package com.dotafriends.dotafriends.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for holding a list of match IDs deserialised from JSON objects fetched using the
 * Dota 2 Web API. Should only be used to temporarily hold match IDs.
 */
public class MatchHistory extends WebApiResult {
    private int numResults;
    private int totalResults;
    private int resultsRemaining;
    private List<MatchHistoryMatch> matches = new ArrayList<>();

    public int getNumResults() {
        return numResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getResultsRemaining() {
        return resultsRemaining;
    }

    public List<MatchHistoryMatch> getMatches() {
        return matches;
    }
}
