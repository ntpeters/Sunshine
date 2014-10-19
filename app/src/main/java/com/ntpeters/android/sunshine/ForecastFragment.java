package com.ntpeters.android.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJson = null;

        String zipCode = "44124";
        String responseType = "json";
        String unitType = "metric";
        int daysToRetrieve = 7;
        String requestUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + zipCode +
                "&mode=" + responseType +
                "&units=" + unitType +
                "&cnt=" + daysToRetrieve;

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" + zipCode + "&mode=json&units=metric&cnt=" + daysToRetrieve);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                forecastJson = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                forecastJson = null;
            }

            forecastJson = buffer.toString();
        } catch (java.io.IOException e) {
            Log.e("ForecastFragment", "Error", e);
            forecastJson = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("PlaceHolderFragment", "Error closing stream", e);
                }
            }
        }

        String[] forecastsArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP - 60/51",
                "Sun - Sunny - 80/68"
        };

        ArrayList<String> forecasts = new ArrayList<String>(Arrays.asList(forecastsArray));

        FragmentActivity activity = getActivity();
        int list_item_layout_id = R.layout.list_item_forecast;
        int text_view_id = R.id.list_item_forecast_textview;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, list_item_layout_id, text_view_id, forecasts);

        ListView listView = (ListView)rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        return rootView;
    }
}
