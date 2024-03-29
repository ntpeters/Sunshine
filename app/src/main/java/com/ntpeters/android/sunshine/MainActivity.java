package com.ntpeters.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }

        Log.v("MainActivity.onCreate", "Created!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity.onDestroy", "Destroyed!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MainActivity.onStart", "Started!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("MainActivity.onStop", "Stopped!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MainActivity.onResume", "Resumed!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MainActivity.onPause", "Paused!");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.action_map) {
            String zip = PreferenceManager
                    .getDefaultSharedPreferences(
                            this)
                    .getString(
                            getString(R.string.pref_location_key),
                            getString(R.string.pref_location_default));

            Uri builtUri = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", zip)
                    .build();

            Intent mapIntent = new Intent();
            mapIntent.setAction(Intent.ACTION_VIEW);
            mapIntent.setData(builtUri);

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
