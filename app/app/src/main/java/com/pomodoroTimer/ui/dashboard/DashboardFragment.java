package com.pomodoroTimer.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pomodoroTimer.MainActivity;
import com.pomodoroTimer.R;


/*
    ORIGINAL JUST-BUILD CONTENT
     */
public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                HistoryComponent history = new HistoryComponent();
                // Organize startActivity
                DashboardFragment historyFragment = new DashboardFragment();
                Intent historyIntent = new Intent(null, HistoryComponent.class);
                // Expectation: Starts the application;
                history.startActivityFromFragment(
                        historyFragment,
                        historyIntent,
                        1,
                        savedInstanceState
                );
            }
        });
        return root;
    }
}