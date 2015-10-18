package com.dotafriends.dotafriends.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchData {
    private long accountId;
    private int playerSlot;
    private int heroId;
    @SerializedName("item_0")
    private int item0;
    @SerializedName("item_1")
    private int item1;
    @SerializedName("item_2")
    private int item2;
    @SerializedName("item_3")
    private int item3;
    @SerializedName("item_4")
    private int item4;
    @SerializedName("item_5")
    private int item5;
    private int kills;
    private int deaths;
    private int assists;
    private int leaverStatus;
    private int gold;
    private int lastHits;
    private int denies;
    private int goldPerMin;
    private int xpPerMin;
    private int goldSpent;
    private int heroDamage;
    private int towerDamage;
    private int heroHealing;
    private int level;
    private List<AbilityUpgrade> abilityUpgrades = new ArrayList<>();

    public long getAccountId() {
        return accountId;
    }

    public int getPlayerSlot() {
        return playerSlot;
    }

    public int getHeroId() {
        return heroId;
    }

    public int getItem0() {
        return item0;
    }

    public int getItem1() {
        return item1;
    }

    public int getItem2() {
        return item2;
    }

    public int getItem3() {
        return item3;
    }

    public int getItem4() {
        return item4;
    }

    public int getItem5() {
        return item5;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getLeaverStatus() {
        return leaverStatus;
    }

    public int getGold() {
        return gold;
    }

    public int getLastHits() {
        return lastHits;
    }

    public int getDenies() {
        return denies;
    }

    public int getGoldPerMin() {
        return goldPerMin;
    }

    public int getXpPerMin() {
        return xpPerMin;
    }

    public int getGoldSpent() {
        return goldSpent;
    }

    public int getHeroDamage() {
        return heroDamage;
    }

    public int getTowerDamage() {
        return towerDamage;
    }

    public int getHeroHealing() {
        return heroHealing;
    }

    public int getLevel() {
        return level;
    }

    public List<AbilityUpgrade> getAbilityUpgrades() {
        return abilityUpgrades;
    }
}
