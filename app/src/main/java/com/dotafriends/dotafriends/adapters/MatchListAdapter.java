package com.dotafriends.dotafriends.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.database.DatabaseContract;
import com.dotafriends.dotafriends.helpers.DataFormatter;

/**
 * Binds data from cursor to ListView in MatchListFragment
 */
public class MatchListAdapter extends CursorAdapter {
    private static final String TAG = "MatchListAdapter";

    Context mContext;

    public MatchListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    // Use a view holder to avoid repeated findViewById calls
    private static class ViewHolder {
        TextView matchId;
        TextView startTime;
        TextView gameMode;
        TextView duration;
        TextView kda;
        ImageView heroIcon;
        View win;
    }

    public View newView(Context context, Cursor c, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_match_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.matchId = (TextView)v.findViewById(R.id.list_item_match_id);
        holder.startTime = (TextView)v.findViewById(R.id.list_item_start_time);
        holder.gameMode = (TextView)v.findViewById(R.id.list_item_game_mode);
        holder.duration = (TextView)v.findViewById(R.id.list_item_duration);
        holder.kda = (TextView)v.findViewById(R.id.list_item_kda);
        holder.heroIcon = (ImageView)v.findViewById(R.id.list_item_hero_icon);
        holder.win = v.findViewById(R.id.list_item_win_loss);
        v.setTag(holder);
        return v;
    }

    public void bindView(View v, Context context, Cursor c) {
        long matchId = c.getLong(c.getColumnIndexOrThrow(DatabaseContract.MatchInfo._ID));
        int startTime = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MatchInfo.START_TIME));
        int gameMode = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MatchInfo.GAME_MODE));
        int duration = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MatchInfo.DURATION));
        int radiantWin = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.MatchInfo.RADIANT_WIN));
        int playerSlot = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData
                .PLAYER_SLOT));
        int heroId = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.HERO_ID));
        int kills = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.KILLS));
        int deaths = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.DEATHS));
        int assists = c.getInt(c.getColumnIndexOrThrow(DatabaseContract.PlayerMatchData.ASSISTS));

        ViewHolder holder = (ViewHolder) v.getTag();
        holder.matchId.setText(String.valueOf(matchId));
        holder.startTime.setText(DataFormatter.formatStartTime(startTime));
        holder.gameMode.setText(DataFormatter.getGameMode(gameMode));
        holder.duration.setText(DataFormatter.formatDuration(duration));
        if (DataFormatter.isWin(playerSlot, radiantWin)) {
            holder.win.setBackgroundColor(0xFF00FF00);
        } else {
            holder.win.setBackgroundColor(0xFFFF0000);
        }
        holder.kda.setText(DataFormatter.formatKda(kills, deaths, assists));
        holder.heroIcon.setImageResource(DataFormatter.getHeroIconDrawable(heroId));
    }

}
