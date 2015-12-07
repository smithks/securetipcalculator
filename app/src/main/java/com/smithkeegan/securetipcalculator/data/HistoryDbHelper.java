package com.smithkeegan.securetipcalculator.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smithkeegan.securetipcalculator.data.HistoryContract.HistoryEntry;

/**
 * Provides helper methods for modifying the history database.
 * Created by Keegan on 11/9/2015.
 */
public class HistoryDbHelper extends SQLiteOpenHelper {

    private static HistoryDbHelper instance;

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather.db";

    public static synchronized HistoryDbHelper getInstance(Context context){
        if (instance==null){
            instance = new HistoryDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HISTORY_TABLE="CREATE TABLE " + HistoryEntry.TABLE_NAME + " ( "+
                HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                HistoryEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
                HistoryEntry.COLUMN_METHOD + " TEXT NOT NULL, "+
                HistoryEntry.COLUMN_BILL+ " REAL NOT NULL, "+
                HistoryEntry.COLUMN_TIP_PERCENT+ " INTEGER NOT NULL, "+
                HistoryEntry.COLUMN_TOTAL+ " REAL NOT NULL, "+
                HistoryEntry.COLUMN_PEOPLE + " INTEGER NOT NULL, "+
                HistoryEntry.COLUMN_EACH_PAYS+ " REAL NOT NULL"+
                " );";

        db.execSQL(SQL_CREATE_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ HistoryEntry.TABLE_NAME);
        onCreate(db);
    }
}
