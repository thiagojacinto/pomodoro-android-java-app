package com.pomodoroTimer.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pomodoroTimer.model.PomodoroTimer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class TimerDAO {

    // Attributes
    private SQLiteDatabase database;
    private String[] columns = {Helper.COLUMN_ID, Helper.COLUMN_LABEL, Helper.COLUMN_DURATION};
    private Helper SQLiteOpenHelper;

    // Constructor
    public TimerDAO(Context context) {
        SQLiteOpenHelper = new Helper(context);
    }

    public void open() throws SQLException {
        database = SQLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        SQLiteOpenHelper.close();
    }

    // -> METHODS

    // Add, Create new
    public PomodoroTimer create(String timerDuration) {
        ContentValues values = new ContentValues();
        values.put(Helper.COLUMN_DURATION, timerDuration);

        long insertId = database.insert(Helper.TABLE_TIMERS, null,
                values);
        Cursor cursor = database.query(Helper.TABLE_TIMERS,
                columns, Helper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        PomodoroTimer newTimer = new PomodoroTimer(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2)
        );

        cursor.close();
        return newTimer;
    }

    // Get all items from DB
    public List<PomodoroTimer> getAll() {
        List<PomodoroTimer> listOfPomodori = new ArrayList<PomodoroTimer>();
        Cursor cursor = database.query(Helper.TABLE_TIMERS ,
                columns , null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PomodoroTimer newTimer = new PomodoroTimer(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            listOfPomodori.add(newTimer);
            cursor.moveToNext();
        }
        cursor.close();
        return listOfPomodori;
    }

    // Delete
    public void delete(PomodoroTimer Timer) {
        long id = Timer.getId();
        database.delete(Helper.TABLE_TIMERS, Helper.COLUMN_ID
                + " = " + id, null);
    }

}
