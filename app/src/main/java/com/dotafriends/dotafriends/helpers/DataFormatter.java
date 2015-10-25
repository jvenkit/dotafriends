package com.dotafriends.dotafriends.helpers;

import android.text.Html;
import android.text.Spanned;

import com.dotafriends.dotafriends.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

/**
 * General purpose helper class for converting and formatting values.
 */
public class DataFormatter {

    private static final HashMap<Integer, String> GAME_MODES = new HashMap<>();
    private static final HashMap<Integer, Integer> HERO_ICONS = new HashMap<>();

    static {
        GAME_MODES.put(0,"None");
        GAME_MODES.put(1,"All Pick");
        GAME_MODES.put(2,"Captain's Mode");
        GAME_MODES.put(3,"Random Draft");
        GAME_MODES.put(4,"Single Draft");
        GAME_MODES.put(5,"All Random");
        GAME_MODES.put(6,"Intro");
        GAME_MODES.put(7,"Diretide");
        GAME_MODES.put(8,"Reverse Captain's Mode");
        GAME_MODES.put(9,"The Greeviling");
        GAME_MODES.put(10,"Tutorial");
        GAME_MODES.put(11,"Mid Only");
        GAME_MODES.put(12,"Least Played");
        GAME_MODES.put(13,"New Player Pool");
        GAME_MODES.put(14,"Compendium Matchmaking");
        GAME_MODES.put(16,"Captain's Draft");
        GAME_MODES.put(17,"Balanced Draft");
        GAME_MODES.put(18,"Ability Draft");
        GAME_MODES.put(19,"?? Event ??");
        GAME_MODES.put(20,"All-Random Deathmatch");
        GAME_MODES.put(21,"1 vs 1 Solo Mid");
        GAME_MODES.put(22,"Ranked All Pick");

        HERO_ICONS.put(1,R.drawable.hero_icon_1);
        HERO_ICONS.put(2,R.drawable.hero_icon_2);
        HERO_ICONS.put(3,R.drawable.hero_icon_3);
        HERO_ICONS.put(4,R.drawable.hero_icon_4);
        HERO_ICONS.put(5,R.drawable.hero_icon_5);
        HERO_ICONS.put(6,R.drawable.hero_icon_6);
        HERO_ICONS.put(7,R.drawable.hero_icon_7);
        HERO_ICONS.put(8,R.drawable.hero_icon_8);
        HERO_ICONS.put(9,R.drawable.hero_icon_9);
        HERO_ICONS.put(10,R.drawable.hero_icon_10);
        HERO_ICONS.put(11,R.drawable.hero_icon_11);
        HERO_ICONS.put(12,R.drawable.hero_icon_12);
        HERO_ICONS.put(13,R.drawable.hero_icon_13);
        HERO_ICONS.put(14,R.drawable.hero_icon_14);
        HERO_ICONS.put(15,R.drawable.hero_icon_15);
        HERO_ICONS.put(16,R.drawable.hero_icon_16);
        HERO_ICONS.put(17,R.drawable.hero_icon_17);
        HERO_ICONS.put(18,R.drawable.hero_icon_18);
        HERO_ICONS.put(19,R.drawable.hero_icon_19);
        HERO_ICONS.put(20,R.drawable.hero_icon_20);
        HERO_ICONS.put(21,R.drawable.hero_icon_21);
        HERO_ICONS.put(22,R.drawable.hero_icon_22);
        HERO_ICONS.put(23,R.drawable.hero_icon_23);
        HERO_ICONS.put(25,R.drawable.hero_icon_25);
        HERO_ICONS.put(26,R.drawable.hero_icon_26);
        HERO_ICONS.put(27,R.drawable.hero_icon_27);
        HERO_ICONS.put(28,R.drawable.hero_icon_28);
        HERO_ICONS.put(29,R.drawable.hero_icon_29);
        HERO_ICONS.put(30,R.drawable.hero_icon_30);
        HERO_ICONS.put(31,R.drawable.hero_icon_31);
        HERO_ICONS.put(32,R.drawable.hero_icon_32);
        HERO_ICONS.put(33,R.drawable.hero_icon_33);
        HERO_ICONS.put(34,R.drawable.hero_icon_34);
        HERO_ICONS.put(35,R.drawable.hero_icon_35);
        HERO_ICONS.put(36,R.drawable.hero_icon_36);
        HERO_ICONS.put(37,R.drawable.hero_icon_37);
        HERO_ICONS.put(38,R.drawable.hero_icon_38);
        HERO_ICONS.put(39,R.drawable.hero_icon_39);
        HERO_ICONS.put(40,R.drawable.hero_icon_40);
        HERO_ICONS.put(41,R.drawable.hero_icon_41);
        HERO_ICONS.put(42,R.drawable.hero_icon_42);
        HERO_ICONS.put(43,R.drawable.hero_icon_43);
        HERO_ICONS.put(44,R.drawable.hero_icon_44);
        HERO_ICONS.put(45,R.drawable.hero_icon_45);
        HERO_ICONS.put(46,R.drawable.hero_icon_46);
        HERO_ICONS.put(47,R.drawable.hero_icon_47);
        HERO_ICONS.put(48,R.drawable.hero_icon_48);
        HERO_ICONS.put(49,R.drawable.hero_icon_49);
        HERO_ICONS.put(50,R.drawable.hero_icon_50);
        HERO_ICONS.put(51,R.drawable.hero_icon_51);
        HERO_ICONS.put(52,R.drawable.hero_icon_52);
        HERO_ICONS.put(53,R.drawable.hero_icon_53);
        HERO_ICONS.put(54,R.drawable.hero_icon_54);
        HERO_ICONS.put(55,R.drawable.hero_icon_55);
        HERO_ICONS.put(56,R.drawable.hero_icon_56);
        HERO_ICONS.put(57,R.drawable.hero_icon_57);
        HERO_ICONS.put(58,R.drawable.hero_icon_58);
        HERO_ICONS.put(59,R.drawable.hero_icon_59);
        HERO_ICONS.put(60,R.drawable.hero_icon_60);
        HERO_ICONS.put(61,R.drawable.hero_icon_61);
        HERO_ICONS.put(62,R.drawable.hero_icon_62);
        HERO_ICONS.put(63,R.drawable.hero_icon_63);
        HERO_ICONS.put(64,R.drawable.hero_icon_64);
        HERO_ICONS.put(65,R.drawable.hero_icon_65);
        HERO_ICONS.put(66,R.drawable.hero_icon_66);
        HERO_ICONS.put(67,R.drawable.hero_icon_67);
        HERO_ICONS.put(68,R.drawable.hero_icon_68);
        HERO_ICONS.put(69,R.drawable.hero_icon_69);
        HERO_ICONS.put(70,R.drawable.hero_icon_70);
        HERO_ICONS.put(71,R.drawable.hero_icon_71);
        HERO_ICONS.put(72,R.drawable.hero_icon_72);
        HERO_ICONS.put(73,R.drawable.hero_icon_73);
        HERO_ICONS.put(74,R.drawable.hero_icon_74);
        HERO_ICONS.put(75,R.drawable.hero_icon_75);
        HERO_ICONS.put(76,R.drawable.hero_icon_76);
        HERO_ICONS.put(77,R.drawable.hero_icon_77);
        HERO_ICONS.put(78,R.drawable.hero_icon_78);
        HERO_ICONS.put(79,R.drawable.hero_icon_79);
        HERO_ICONS.put(80,R.drawable.hero_icon_80);
        HERO_ICONS.put(81,R.drawable.hero_icon_81);
        HERO_ICONS.put(82,R.drawable.hero_icon_82);
        HERO_ICONS.put(83,R.drawable.hero_icon_83);
        HERO_ICONS.put(84,R.drawable.hero_icon_84);
        HERO_ICONS.put(85,R.drawable.hero_icon_85);
        HERO_ICONS.put(86,R.drawable.hero_icon_86);
        HERO_ICONS.put(87,R.drawable.hero_icon_87);
        HERO_ICONS.put(88,R.drawable.hero_icon_88);
        HERO_ICONS.put(89,R.drawable.hero_icon_89);
        HERO_ICONS.put(90,R.drawable.hero_icon_90);
        HERO_ICONS.put(91,R.drawable.hero_icon_91);
        HERO_ICONS.put(92,R.drawable.hero_icon_92);
        HERO_ICONS.put(93,R.drawable.hero_icon_93);
        HERO_ICONS.put(94,R.drawable.hero_icon_94);
        HERO_ICONS.put(95,R.drawable.hero_icon_95);
        HERO_ICONS.put(96,R.drawable.hero_icon_96);
        HERO_ICONS.put(97,R.drawable.hero_icon_97);
        HERO_ICONS.put(98,R.drawable.hero_icon_98);
        HERO_ICONS.put(99,R.drawable.hero_icon_99);
        HERO_ICONS.put(100,R.drawable.hero_icon_100);
        HERO_ICONS.put(101,R.drawable.hero_icon_101);
        HERO_ICONS.put(102,R.drawable.hero_icon_102);
        HERO_ICONS.put(103,R.drawable.hero_icon_103);
        HERO_ICONS.put(104,R.drawable.hero_icon_104);
        HERO_ICONS.put(105,R.drawable.hero_icon_105);
        HERO_ICONS.put(106,R.drawable.hero_icon_106);
        HERO_ICONS.put(107,R.drawable.hero_icon_107);
        HERO_ICONS.put(109,R.drawable.hero_icon_109);
        HERO_ICONS.put(110,R.drawable.hero_icon_110);
        HERO_ICONS.put(111,R.drawable.hero_icon_111);
        HERO_ICONS.put(112,R.drawable.hero_icon_112);
    }

    /**
     * Returns a game mode String. The gameMode argument is an integer that
     * corresponds to a game mode.
     */
    public static String getGameMode(int gameMode) {
        return GAME_MODES.containsKey(gameMode) ? GAME_MODES.get(gameMode) : "Unknown game mode";
    }

    /**
     * Returns the resource ID of the hero icon corresponding to the given hero ID
     */
    public static int getHeroIconDrawable(int heroId) {
        return HERO_ICONS.containsKey(heroId) ? HERO_ICONS.get(heroId) : R.drawable.hero_icon_default;
    }

    /**
     * Returns a date/time string. The startTime argument must be given in the
     * form of a Unix timestamp (seconds since Jan 01 1970 (UTC))
     */
    public static String formatStartTime(int startTime) {
        long time = (long)startTime * 1000;
        Date date = new Date(time);
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(date);
    }

    /**
     * Returns the duration of a game in a formatted String showing minutes and
     * hours. The duration argument is an integer representing the duration of a
     * match in seconds
     */
    public static String formatDuration(int duration) {
        int minutes = duration / 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        int seconds = duration % 60;

        return (hours > 0 ? String.valueOf(hours) + ":" : "") +
                (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes)) + ":" +
                (seconds < 10 ? "0" + String.valueOf(seconds) : String.valueOf(seconds));
    }

    /**
     * Returns a boolean representing whether or not the player in the given slot won the game
     */
    public static boolean isWin(int playerSlot, int radiantWin) {
        if (playerSlot < 5) {
            return (radiantWin > 0);
        } else {
            return (!(radiantWin > 0));
        }
    }

    /**
     * Returns a formatted string representing kills, deaths and assists
     */
    public static Spanned formatKda(int kills, int deaths, int assists) {
        String kda = new Formatter().format("<font color=#00FF00>%d</font> / <font color=#FF0000>%d</font> / <font color=#0000FF>%d</font>",
                kills, deaths, assists).toString();
        return Html.fromHtml(kda);
    }

    public static String get64BitSteamId(long accountId) {
        String binary = "00000001" + "0001" + "00000000000000000001" + Long.toBinaryString(accountId | 0x100000000L).substring(1);
        return String.valueOf(Long.parseLong(binary, 2));
    }

    public static Long getAccountId(String steamId) {
        return Long.parseLong(steamId) & 0xFFFFFFFFL;
    }
}
