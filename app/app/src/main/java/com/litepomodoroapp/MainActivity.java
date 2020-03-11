package com.litepomodoroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.litepomodoroapp.utils.PrefUtil;

import java.util.Calendar;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonPause, buttonStop, buttonSettings;
    TextView tvCountdown;
    MaterialProgressBar progressBar;

    // Countdown specific attributes
    public enum TimerState {
        Running, Paused, Stopped;
    }
    private CountDownTimer countdownTimer;
    private long timerLengthSeconds = 0L;
    private TimerState timerState = TimerState.Stopped;

    private long secondsRemaining = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_countdown);
        tvCountdown = findViewById(R.id.tv_timer_countdown);
        buttonSettings = findViewById(R.id.button_settings);

        // clicking on SETTINGS
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettingsIntent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(goToSettingsIntent);
            }
        });

        // clicking in START
        buttonStart = findViewById(R.id.button_play);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                timerState = TimerState.Running;
                updateButtons();
            }
        });

        // clicking in PAUSE
        buttonStart = findViewById(R.id.button_pause);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownTimer.cancel();
                timerState = TimerState.Paused;
                updateButtons();
            }
        });

        // clicking in STOP
        buttonStart = findViewById(R.id.button_stop);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownTimer.cancel();
                onTimerFinished();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initTimer();

        //TODO: hide notification

        // Removing alarm
        removeAlarm(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (timerState == TimerState.Running) {
            countdownTimer.cancel();

            // background handling
            long wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining);
            //TODO: show notification
        } else if (timerState == TimerState.Paused) {
            //TODO: just show notification when paused
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this);
        PrefUtil.setSecondsRemaining(secondsRemaining, this);
        PrefUtil.setTimerState(timerState, this);
    }

    private void initTimer() {
        timerState = PrefUtil.getTimerState(this);

        if (timerState == TimerState.Stopped) {
            setNewTimerLength();
        } else {
            setPreviousTimerLength();
        }

        // Setting remaining seconds:
        secondsRemaining =
                (timerState == TimerState.Running || timerState == TimerState.Paused) ?
                        PrefUtil.getSecondsRemaining(this)
                        : timerLengthSeconds;

        //TODO: change secondsRemaining according to where background timer stopped;
        long alarmSetTime = PrefUtil.getAlarmSetTime(this);
        if (alarmSetTime > 0) {
            secondsRemaining -= nowSeconds - alarmSetTime;
        }
        if (secondsRemaining <= 0) {
            onTimerFinished();
        } else if (timerState == TimerState.Running) { startTimer(); }

        updateButtons();
        updateCountdownUI();
    }

    private void onTimerFinished(){
        timerState = TimerState.Stopped;

        setNewTimerLength();
        progressBar.setProgress(0);

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this);
        secondsRemaining = timerLengthSeconds;

        // Starts vibrating
        Vibrator vibrateNow = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrateNow.hasVibrator()) vibrateNow.vibrate(3000);
        Log.d("Log", "onTimerFinished: starts vibrating now...");
        vibrateNow.cancel();    // Stops vibration

        updateButtons();
        updateCountdownUI();
    }

    private void startTimer() {
        timerState = TimerState.Running;
        countdownTimer = new CountDownTimer(secondsRemaining * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = millisUntilFinished / 1000;
                updateCountdownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        }.start();
    }

    private void setNewTimerLength(){
        int lengthInMinutes = PrefUtil.getTimerLength(this);
        timerLengthSeconds = lengthInMinutes * 60L;
        progressBar.setMax((int) timerLengthSeconds);
    }

    private void setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this);
        progressBar.setMax((int) timerLengthSeconds);
    }

    private void updateCountdownUI(){
        long minutesUntilFinished = secondsRemaining / 60;
        long secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60;
        String secondsString = String.valueOf(secondsInMinutesUntilFinished);

        // Update textView text
        String updatedSeconds = (secondsString.length() == 2) ?
                secondsString
                : "0" + secondsString;
        String updatedTime = minutesUntilFinished + ":" + updatedSeconds;

        tvCountdown.setText(updatedTime);
        progressBar.setProgress((int) (timerLengthSeconds - secondsRemaining));
    }

    private void updateButtons() {

        buttonStop = findViewById(R.id.button_stop);
        buttonStart = findViewById(R.id.button_play);
        buttonPause = findViewById(R.id.button_pause);

        switch (timerState) {
            case Running:
                buttonStart.setEnabled(false);
                buttonPause.setEnabled(true);
                buttonStop.setEnabled(true);
                break;
            case Stopped:
                buttonStart.setEnabled(true);
                buttonPause.setEnabled(false);
                buttonStop.setEnabled(false);
                break;
            case Paused:
                buttonStart.setEnabled(true);
                buttonPause.setEnabled(false);
                buttonStop.setEnabled(true);
                break;
        }

    }

    /*
     -> BACKGROUND ACTIVITIES
    */

    public static long nowSeconds = Calendar.getInstance().getTimeInMillis() / 1000;

    public static long setAlarm(Context context, long nowSeconds, long secondsRemaining) {
        long wakeUpTime = (nowSeconds + secondsRemaining) * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Intent to specifies what is going to do when alarm goes off
        Intent afterAlarm = new Intent(context, TimeReceiver.class);
        // Pending Intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, afterAlarm, 0);

        // Sets alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent);
        }

        PrefUtil.setAlarmSetTime(nowSeconds, context);
        return wakeUpTime;
    }

    public static void removeAlarm(Context context){
        Intent removeAlarmIntent = new Intent(context, TimeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, removeAlarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        PrefUtil.setAlarmSetTime(0, context);   // 0 => alarm is NOT set;
    }
}
