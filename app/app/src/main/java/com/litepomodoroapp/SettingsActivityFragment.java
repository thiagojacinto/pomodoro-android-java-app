package com.litepomodoroapp;

import android.app.Fragment;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivityFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
