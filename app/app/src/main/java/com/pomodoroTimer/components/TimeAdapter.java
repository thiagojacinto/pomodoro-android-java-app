package com.pomodoroTimer.components;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.pomodoroTimer.R;
import com.pomodoroTimer.model.PomodoroTimer;

import java.util.List;

public class TimeAdapter extends ArrayAdapter<PomodoroTimer> {

    // Attributes
    Context myOwnContext;
    int listFromLayout;
    List<PomodoroTimer> listOfTimers;
    SQLiteDatabase dbOfTimers;
    String dbName;

    // Necessary Constructor
    public TimeAdapter(Context myOwnContext, int listFromLayout, List<PomodoroTimer> listOfTimers, SQLiteDatabase dbOfTimers, String databaseName) {
        super(myOwnContext, listFromLayout, listOfTimers);

        this.myOwnContext = myOwnContext;
        this.listFromLayout = listFromLayout;
        this.listOfTimers = listOfTimers;
        this.dbOfTimers = dbOfTimers;
        this.dbName = databaseName;
    }

    // View METHODS
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parentView) {

        // Attributes
        LayoutInflater inflater = LayoutInflater.from(myOwnContext);
        View view = inflater.inflate(listFromLayout, null);

        // Position of current position in `listOfTimers`:
        final PomodoroTimer timerItem = listOfTimers.get(position);

        // Get the Views from Layout
        TextView textViewDuration = view.findViewById(R.id.tv_duration);
        TextView textViewLabel = view.findViewById(R.id.tv_label);
        TextView textViewId = view.findViewById(R.id.tv_key_code);

        // Set the values from List
        textViewDuration.setText(timerItem.getDuration());
        textViewLabel.setText(timerItem.getLabel());
        textViewId.setText(String.valueOf(timerItem.getId()));

        // Get Button(s)
        Button buttonDelete = view.findViewById(R.id.button_delete);

        /*
        METHODS
         */

        // Delete from list view

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(myOwnContext);
                alert.setTitle("@string/delete_confirm");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM " + dbName + " WHERE id = ?";
                        dbOfTimers.execSQL(sql, new Long[]{timerItem.getId()});
                        reloadItemsFromDb();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // DOES NOTHING WHEN `Cancel` IS CLICKED
                    }
                });
                AlertDialog deleteAlert = alert.create();
                deleteAlert.show();
            }
        });
        
        // Returns the `view`
        return view;
    }

    private void reloadItemsFromDb() {
        Cursor cursorOnList = dbOfTimers.rawQuery("SELECT * FROM " + dbName, null);

        if (cursorOnList.moveToFirst()) {
            listOfTimers.clear();   // Erases list of timers

            do {
                listOfTimers.add(new PomodoroTimer(
                        // Reads line per line of DB
                        cursorOnList.getLong(0),
                        cursorOnList.getString(1),
                        cursorOnList.getString(2)
                ));
            } while (cursorOnList.moveToNext());
        }
        cursorOnList.close();   // closes connection with DB reading operation
        notifyDataSetChanged();
    }

}
