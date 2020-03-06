package com.litepomodoroapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.litepomodoroapp.MainActivity;

public class PrefUtil {

    public static int getTimerLength(Context context) {
        //TODO add functionality to runs Countdown @ background
        return 1;
    }

    private static final String PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.litepomodoroapp.previous_timer_length_seconds_id";

    public static long getPreviousTimerLengthSeconds(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0);
    }

    public static void setPreviousTimerLengthSeconds(long seconds, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds);
        editor.apply();

    }

    private static final String TIMER_STATE_ID = "com.litepomodoroapp.timer_state";

    public static MainActivity.TimerState getTimerState(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int ordinal = preferences.getInt(TIMER_STATE_ID, 0);

        return MainActivity.TimerState.values()[ordinal];
    }

    public static void setTimerState(MainActivity.TimerState timerState, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        int ordinal = timerState.ordinal();
        editor.putInt(TIMER_STATE_ID, ordinal);
        editor.apply();

    }

    static final String SECONDS_REMAINING = "com.litepomodoroapp.seconds_remaining";

    public static long getSecondsRemaining(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(SECONDS_REMAINING, 0);
    }

    public static void setSecondsRemaining(long seconds, Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(SECONDS_REMAINING, seconds);
        editor.apply();

    }
}