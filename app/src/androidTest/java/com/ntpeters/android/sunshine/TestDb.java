package com.ntpeters.android.sunshine;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.ntpeters.android.sunshine.data.WeatherDbHelper;

/**
 * Created by Nate on 10/29/2014.
 */
public class TestDb  extends AndroidTestCase{
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
}
