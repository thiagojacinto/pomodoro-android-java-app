package com.pomodoroTimer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Helper extends SQLiteOpenHelper {

    // Attributes
    public static final String TABLE_TIMERS = "timers";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DURATION = "duration";

    private static final String DATABASE_NAME = "timers.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public Helper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Methods
    // -> Creates the table
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TIMERS + "(" + COLUMN_ID
            + " integer primary key autoincrement , " + COLUMN_LABEL
            + " text not null , " + COLUMN_DURATION
            + " text not null"
            + ");";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // -> Uses the created string to execute the SQL statement
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    // -> Used to erase equal SQL table if it is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);
        onCreate(db);
    }
}
