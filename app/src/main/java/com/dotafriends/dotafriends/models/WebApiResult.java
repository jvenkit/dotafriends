package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

/**
 * Format of data returned by the Steam Web API
 */
public class WebApiResult {
    public String error;
    @SerializedName("statusDetail")
    public String statusDetail;
}
