package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

public class PlayerSummary {
    @SerializedName("steamid")
    private String steamId;
    @SerializedName("personaname")
    private String personaName;
    @SerializedName("profileurl")
    private String profileUrl;
    private String avatar;
    @SerializedName("avatarmedium")
    private String avatarMedium;
    @SerializedName("avatarfull")
    private String avatarFull;
    @SerializedName("timecreated")
    private int timeCreated;

    public String getSteamId() {
        return steamId;
    }

    public String getPersonaName() {
        return personaName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAvatarMedium() {
        return avatarMedium;
    }

    public String getAvatarFull() {
        return avatarFull;
    }

    public int getTimeCreated() {
        return timeCreated;
    }
}
