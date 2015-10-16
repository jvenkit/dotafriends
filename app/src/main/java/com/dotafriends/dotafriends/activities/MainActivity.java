package com.dotafriends.dotafriends.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dotafriends.dotafriends.R;
import com.dotafriends.dotafriends.fragments.MatchListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String SETTINGS = "settings";
    public static final String PLAYER_ID = "PlayerId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.app.FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            Fragment matchListFragment = new MatchListFragment();
            fm.beginTransaction().add(R.id.fragment_container, matchListFragment)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case (R.id.action_settings):
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
