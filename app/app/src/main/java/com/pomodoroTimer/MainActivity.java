package com.pomodoroTimer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pomodoroTimer.components.TimeAdapter;
import com.pomodoroTimer.dao.TimerDAO;
import com.pomodoroTimer.model.PomodoroTimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Attributes
    public static final String DATABASE_NAME = "timers.db";

    List<PomodoroTimer> listOfTimers;
    SQLiteDatabase dbOfTimers;
    ListView listViewOfTimers;
    TimeAdapter timeAdapter;
    TimerDAO timerDAO;
    TextView buttonStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
            [init] nav configuration
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*
            [improv] history handling
         */

        setContentView(R.layout.fragment_dashboard);
        listViewOfTimers = findViewById(R.id.hist_list);
        listOfTimers = new ArrayList<>();

        setContentView(R.layout.fragment_home);
        buttonStart = findViewById(R.id.text_view_start);

        // Instantiate DAO
        timerDAO = new TimerDAO(this);

        try {
            timerDAO.open();

            // Clicking in START
            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newTimerDuration = new PomodoroTimer().getDEFAULT_25_MINUTES();
                    timerDAO.create(newTimerDuration);
                    finish();
                }
            });

        } catch (SQLException error) {
            error.printStackTrace();
        }
        /*
        // Calling DB
        dbOfTimers = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        // Then, shows db entries
        showTimersFromDB();

         */
    }

    @Override
    protected void onResume() {
        try {
            timerDAO.open();
            super.onResume();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        timerDAO.close();
        super.onPause();
    }

    private void showTimersFromDB() {
        // Similar to Adapter`s reloadFromDB:
        String sqlCommand = "SELECT * FROM " + MainActivity.DATABASE_NAME + "";
        Cursor timerCursor = dbOfTimers.rawQuery(sqlCommand, null);

        // a forEach approach to the db items:
        if (timerCursor.moveToFirst()) {
            // start a loop:
            do {

                listOfTimers.add(new PomodoroTimer(
                        // Reads line per line of DB
                        timerCursor.getLong(0),
                        timerCursor.getString(1),
                        timerCursor.getString(2)
                ));
            } while (timerCursor.moveToNext());
        }
        // Closing cursor
        timerCursor.close();

        // Instantiate the custom adapter
        timeAdapter = new TimeAdapter(this,
                R.layout.list_layout_timer,
                listOfTimers,
                dbOfTimers,
                MainActivity.DATABASE_NAME
        );
        // set this adapter into listView:
        listViewOfTimers.setAdapter(timeAdapter);
    }

}
