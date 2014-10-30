package com.ntpeters.android.sunshine;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.ntpeters.android.sunshine.data.WeatherContract.*;
import com.ntpeters.android.sunshine.data.WeatherDbHelper;

/**
 * Created by Nate on 10/29/2014.
 */
public class TestDb  extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        String testName = "North Pole";
        String testLocationSetting= "99705";
        double testLatitude = 64.772;
        double testLongitude = -147.355;

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_CITY_NAME, testName);
        values.put(LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(LocationEntry.COLUMN_COORD_LONG, testLongitude);

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, values);

        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        String[] columns = {
                LocationEntry._ID,
                LocationEntry.COLUMN_LOCATION_SETTING,
                LocationEntry.COLUMN_CITY_NAME,
                LocationEntry.COLUMN_COORD_LAT,
                LocationEntry.COLUMN_COORD_LONG
        };

        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int locationIndex = cursor.getColumnIndex(LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex(LocationEntry.COLUMN_CITY_NAME);
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex(LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);

            assertEquals(testName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
            weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
            weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
            weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
            weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
            weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
            weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
            weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
            weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
            weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

            long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
            assertTrue(weatherRowId != -1);

            // A cursor is your primary interface to the query results.
            Cursor weatherCursor = db.query(
                    WeatherEntry.TABLE_NAME,  // Table to Query
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    null  // sort order
            );

            if (!weatherCursor.moveToFirst()) {
                fail("No weather data returned!");
            }

            assertEquals(weatherCursor.getInt(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_LOC_KEY)), locationRowId);
            assertEquals(weatherCursor.getString(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DATETEXT)), "20141205");
            assertEquals(weatherCursor.getDouble(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_DEGREES)), 1.1);
            assertEquals(weatherCursor.getDouble(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY)), 1.2);
            assertEquals(weatherCursor.getDouble(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_PRESSURE)), 1.3);
            assertEquals(weatherCursor.getInt(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MAX_TEMP)), 75);
            assertEquals(weatherCursor.getInt(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_MIN_TEMP)), 65);
            assertEquals(weatherCursor.getString(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_SHORT_DESC)), "Asteroids");
            assertEquals(weatherCursor.getDouble(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WIND_SPEED)), 5.5);
            assertEquals(weatherCursor.getInt(
                    weatherCursor.getColumnIndex(WeatherEntry.COLUMN_WEATHER_ID)), 321);

            weatherCursor.close();
            dbHelper.close();
        } else {
            fail("No values returned!");
        }
    }
}
