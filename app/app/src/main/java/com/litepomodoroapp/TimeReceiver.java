package com.litepomodoroapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.litepomodoroapp.utils.PrefUtil;

public class TimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: Notification to it

        // When timer expires, change state and clear alarm
        PrefUtil.setTimerState(MainActivity.TimerState.Stopped, context);
        PrefUtil.setAlarmSetTime(0, context);
    }
}
