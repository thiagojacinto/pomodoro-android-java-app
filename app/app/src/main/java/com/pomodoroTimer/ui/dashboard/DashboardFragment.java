package com.pomodoroTimer.ui.dashboard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.pomodoroTimer.MainActivity;
import com.pomodoroTimer.R;
import com.pomodoroTimer.components.TimeAdapter;
import com.pomodoroTimer.model.PomodoroTimer;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends AppCompatActivity {

    List<PomodoroTimer> listOfTimers;
    SQLiteDatabase dbOfTimers;
    ListView listViewOfTimers;
    TimeAdapter timeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Uses `super` & set content to `HISTORY` layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        listViewOfTimers = findViewById(R.id.hist_list);
        listOfTimers = new ArrayList<>();

        // Calling DB
        dbOfTimers = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        // Then, shows db entries
        showTimersFromDB();
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

/*
    ORIGINAL JUST-BUILD CONTENT
     */
//public class DashboardFragment extends Fragment {

//    private DashboardViewModel dashboardViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        dashboardViewModel =
//                ViewModelProviders.of(this).get(DashboardViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_view_unseen);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }