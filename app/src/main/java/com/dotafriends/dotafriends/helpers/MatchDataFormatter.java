package com.dotafriends.dotafriends.helpers;

import android.text.Html;
import android.text.Spanned;

import com.dotafriends.dotafriends.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * Provides methods for converting values retrieved from the database to user facing objects such as
 * Strings or resource IDs.
 */
public class MatchDataFormatter {

    /**
     * Returns a game mode String. The gameMode argument is an integer that
     * corresponds to a game mode.
     */
    public static String formatGameMode(int gameMode) {
        switch (gameMode) {
            case 0:
                return "None";
            case 1:
                return "All Pick";
            case 2:
                return "Captain's Mode";
            case 3:
                return "Random Draft";
            case 4:
                return "Single Draft";
            case 5:
                return "All Random";
            case 6:
                return "Intro";
            case 7:
                return "Diretide";
            case 8:
                return "Reverse Captain's Mode";
            case 9:
                return "The Greeviling";
            case 10:
                return "Tutorial";
            case 11:
                return "Mid Only";
            case 12:
                return "Least Played";
            case 13:
                return "New Player Pool";
            case 14:
                return "Compendium Matchmaking";
            case 16:
                return "Captain's Draft";
            case 17:
                return "Balanced Draft";
            case 18:
                return "Ability Draft";
            case 19:
                return "?? Event ??";
            case 20:
                return "All-Random Deathmatch";
            case 21:
                return "1 vs 1 Solo Mid";
            case 22:
                return "Ranked All Pick";
            default:
                return "Invalid Game Mode";
        }
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
    public static boolean formatWin(int playerSlot, int radiantWin) {
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

    /**
     * Returns the resource ID of the hero icon corresponding to the given hero ID
     */
    public static int getHeroIconDrawable(int heroId) {
        switch (heroId) {
            case 1:
                return R.drawable.hero_icon_1;
            case 2:
                return R.drawable.hero_icon_2;
            case 3:
                return R.drawable.hero_icon_3;
            case 4:
                return R.drawable.hero_icon_4;
            case 5:
                return R.drawable.hero_icon_5;
            case 6:
                return R.drawable.hero_icon_6;
            case 7:
                return R.drawable.hero_icon_7;
            case 8:
                return R.drawable.hero_icon_8;
            case 9:
                return R.drawable.hero_icon_9;
            case 10:
                return R.drawable.hero_icon_10;
            case 11:
                return R.drawable.hero_icon_11;
            case 12:
                return R.drawable.hero_icon_12;
            case 13:
                return R.drawable.hero_icon_13;
            case 14:
                return R.drawable.hero_icon_14;
            case 15:
                return R.drawable.hero_icon_15;
            case 16:
                return R.drawable.hero_icon_16;
            case 17:
                return R.drawable.hero_icon_17;
            case 18:
                return R.drawable.hero_icon_18;
            case 19:
                return R.drawable.hero_icon_19;
            case 20:
                return R.drawable.hero_icon_20;
            case 21:
                return R.drawable.hero_icon_21;
            case 22:
                return R.drawable.hero_icon_22;
            case 23:
                return R.drawable.hero_icon_23;
            case 25:
                return R.drawable.hero_icon_25;
            case 26:
                return R.drawable.hero_icon_26;
            case 27:
                return R.drawable.hero_icon_27;
            case 28:
                return R.drawable.hero_icon_28;
            case 29:
                return R.drawable.hero_icon_29;
            case 30:
                return R.drawable.hero_icon_30;
            case 31:
                return R.drawable.hero_icon_31;
            case 32:
                return R.drawable.hero_icon_32;
            case 33:
                return R.drawable.hero_icon_33;
            case 34:
                return R.drawable.hero_icon_34;
            case 35:
                return R.drawable.hero_icon_35;
            case 36:
                return R.drawable.hero_icon_36;
            case 37:
                return R.drawable.hero_icon_37;
            case 38:
                return R.drawable.hero_icon_38;
            case 39:
                return R.drawable.hero_icon_39;
            case 40:
                return R.drawable.hero_icon_40;
            case 41:
                return R.drawable.hero_icon_41;
            case 42:
                return R.drawable.hero_icon_42;
            case 43:
                return R.drawable.hero_icon_43;
            case 44:
                return R.drawable.hero_icon_44;
            case 45:
                return R.drawable.hero_icon_45;
            case 46:
                return R.drawable.hero_icon_46;
            case 47:
                return R.drawable.hero_icon_47;
            case 48:
                return R.drawable.hero_icon_48;
            case 49:
                return R.drawable.hero_icon_49;
            case 50:
                return R.drawable.hero_icon_50;
            case 51:
                return R.drawable.hero_icon_51;
            case 52:
                return R.drawable.hero_icon_52;
            case 53:
                return R.drawable.hero_icon_53;
            case 54:
                return R.drawable.hero_icon_54;
            case 55:
                return R.drawable.hero_icon_55;
            case 56:
                return R.drawable.hero_icon_56;
            case 57:
                return R.drawable.hero_icon_57;
            case 58:
                return R.drawable.hero_icon_58;
            case 59:
                return R.drawable.hero_icon_59;
            case 60:
                return R.drawable.hero_icon_60;
            case 61:
                return R.drawable.hero_icon_61;
            case 62:
                return R.drawable.hero_icon_62;
            case 63:
                return R.drawable.hero_icon_63;
            case 64:
                return R.drawable.hero_icon_64;
            case 65:
                return R.drawable.hero_icon_65;
            case 66:
                return R.drawable.hero_icon_66;
            case 67:
                return R.drawable.hero_icon_67;
            case 68:
                return R.drawable.hero_icon_68;
            case 69:
                return R.drawable.hero_icon_69;
            case 70:
                return R.drawable.hero_icon_70;
            case 71:
                return R.drawable.hero_icon_71;
            case 72:
                return R.drawable.hero_icon_72;
            case 73:
                return R.drawable.hero_icon_73;
            case 74:
                return R.drawable.hero_icon_74;
            case 75:
                return R.drawable.hero_icon_75;
            case 76:
                return R.drawable.hero_icon_76;
            case 77:
                return R.drawable.hero_icon_77;
            case 78:
                return R.drawable.hero_icon_78;
            case 79:
                return R.drawable.hero_icon_79;
            case 80:
                return R.drawable.hero_icon_80;
            case 81:
                return R.drawable.hero_icon_81;
            case 82:
                return R.drawable.hero_icon_82;
            case 83:
                return R.drawable.hero_icon_83;
            case 84:
                return R.drawable.hero_icon_84;
            case 85:
                return R.drawable.hero_icon_85;
            case 86:
                return R.drawable.hero_icon_86;
            case 87:
                return R.drawable.hero_icon_87;
            case 88:
                return R.drawable.hero_icon_88;
            case 89:
                return R.drawable.hero_icon_89;
            case 90:
                return R.drawable.hero_icon_90;
            case 91:
                return R.drawable.hero_icon_91;
            case 92:
                return R.drawable.hero_icon_92;
            case 93:
                return R.drawable.hero_icon_93;
            case 94:
                return R.drawable.hero_icon_94;
            case 95:
                return R.drawable.hero_icon_95;
            case 96:
                return R.drawable.hero_icon_96;
            case 97:
                return R.drawable.hero_icon_97;
            case 98:
                return R.drawable.hero_icon_98;
            case 99:
                return R.drawable.hero_icon_99;
            case 100:
                return R.drawable.hero_icon_100;
            case 101:
                return R.drawable.hero_icon_101;
            case 102:
                return R.drawable.hero_icon_102;
            case 103:
                return R.drawable.hero_icon_103;
            case 104:
                return R.drawable.hero_icon_104;
            case 105:
                return R.drawable.hero_icon_105;
            case 106:
                return R.drawable.hero_icon_106;
            case 107:
                return R.drawable.hero_icon_107;
            case 109:
                return R.drawable.hero_icon_109;
            case 110:
                return R.drawable.hero_icon_110;
            case 111:
                return R.drawable.hero_icon_111;
            case 112:
                return R.drawable.hero_icon_112;
            default:
                return R.drawable.hero_icon_default;
        }
    }
}
