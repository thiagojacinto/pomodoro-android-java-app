package com.pomodoroTimer.model;

import java.util.Random;

public class PomodoroTimer {

    // Attributes
    private long id;
    private String label, duration;
    private static final String DEFAULT_25_MINUTES = "25";

    // Constructor:
    //
    // (a) default 25-minute;
    public PomodoroTimer(long id, String label) {
        // Constant duration of 25 minutes

        this.id = id;
        this.label = label;
        this.duration = DEFAULT_25_MINUTES;
    }

    // (b) customized duration;
    public PomodoroTimer(long id, String label, String duration) {
        this.id = id;
        this.label = label;
        this.duration = duration;
    }

    // (c) empty
    public PomodoroTimer() {
        this.id = new Random().nextLong()*10000L;
        this.label = "Default Timer";
        this.duration = getDEFAULT_25_MINUTES();
    }

    // GETTERS

    public String getDuration() {
        return duration;
    }

    public String getLabel() {
        return label;
    }

    public long getId() {
        return id;
    }

    public String getDEFAULT_25_MINUTES() {
        return DEFAULT_25_MINUTES;
    }
}
