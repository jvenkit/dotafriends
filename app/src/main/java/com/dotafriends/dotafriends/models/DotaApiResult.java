package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

/**
 * Format of data returned by the Dota Web API
 */
public class DotaApiResult {
    private String error;
    @SerializedName("statusDetail")
    private String statusDetail;

    public String getError() {
        return error;
    }

    public String getStatusDetail() {
        return statusDetail;
    }
}
