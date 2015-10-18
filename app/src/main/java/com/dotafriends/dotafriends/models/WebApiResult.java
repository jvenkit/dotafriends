package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

/**
 * Format of data returned by the Steam Web API
 */
public class WebApiResult {
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
